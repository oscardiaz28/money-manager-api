package com.odiaz.moneymanager.service;

import com.odiaz.moneymanager.dto.profile.AuthDTO;
import com.odiaz.moneymanager.dto.profile.ProfileDTO;
import com.odiaz.moneymanager.dto.profile.ProfileMapper;
import com.odiaz.moneymanager.model.ProfileEntity;
import com.odiaz.moneymanager.model.UserDetailsImpl;
import com.odiaz.moneymanager.repository.ProfileRepository;
import com.odiaz.moneymanager.util.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@Service
public class ProfileService {

    @Value("${app.activation.url}")
    private String activationUrl;

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final SpringTemplateEngine templateEngine;

    public ProfileService(ProfileRepository profileRepository, ProfileMapper profileMapper, EmailService emailService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils, SpringTemplateEngine templateEngine) {
        this.profileRepository = profileRepository;
        this.profileMapper = profileMapper;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.templateEngine = templateEngine;
    }

    public ProfileDTO registerProfile(ProfileDTO body){
        Optional<ProfileEntity> isEmailTaken = profileRepository.findByEmail(body.getEmail());
        if(isEmailTaken.isPresent()){
            throw new RuntimeException("El email ya esta en uso");
        }
        ProfileEntity newProfile = profileMapper.toEntity(body);
        newProfile.setActivationToken(UUID.randomUUID().toString());
        newProfile.setPassword(passwordEncoder.encode(newProfile.getPassword()));

        ProfileEntity saved = profileRepository.save(newProfile);
        // send activation email
        String activationLink = activationUrl + "/activate?token="+newProfile.getActivationToken();
        String subject = "Active su cuenta en Money Manager";
        String mailBody = "Haga clic en el siguiente enlace para activar su cuenta " + activationLink;

        Context context = new Context();
        context.setVariable("profileName", saved.getFullName());
        context.setVariable("activationLink", activationLink);
        String htmlContent = templateEngine.process("activation-template", context);

        emailService.sendEmail(newProfile.getEmail(), subject, htmlContent);
        return profileMapper.toDTO(saved);
    }

    public ProfileEntity findByEmail(String email){
        return profileRepository.findByEmail(email)
                .orElseThrow( () -> new RuntimeException("El usuario no ha sido encontrado") );
    }

    public boolean activateProfile(String activationToken){
        return profileRepository.findByActivationToken(activationToken)
                .map( p -> {
                    p.setIsActive(true);
                    p.setActivationToken(null);
                    profileRepository.save(p);
                    return true;
                })
                .orElse(false);
    }

    public boolean isAccountActive(String email){
        return profileRepository.findByEmail(email)
                .map( ProfileEntity::getIsActive )
                .orElse(false);
    }

    public ProfileEntity getUserLogged(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String email = userDetails.getUsername();
        return profileRepository.findByEmail(email)
                .orElseThrow( () -> new RuntimeException("El usuario no ha sido encontrado") );
    }

    public Map<String, Object> authAndGenerateToken(AuthDTO authDTO) {

        ProfileEntity existProfile = this.findByEmail(authDTO.getEmail());

        if( !existProfile.getIsActive() ){
            throw new RuntimeException("Account is not active. Please activate your account first");
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDTO.getEmail(), authDTO.getPassword()));

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ProfileEntity profile = userDetails.getProfile();

        String token = jwtUtils.generateToken(profile.getEmail());

        return Map.of(
                "token", token,
                "user", profileMapper.toDTO(profile)
        );

    }

}
