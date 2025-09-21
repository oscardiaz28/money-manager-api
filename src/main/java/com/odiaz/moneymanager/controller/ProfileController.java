package com.odiaz.moneymanager.controller;

import com.odiaz.moneymanager.dto.profile.AuthDTO;
import com.odiaz.moneymanager.dto.profile.ProfileDTO;
import com.odiaz.moneymanager.dto.profile.ProfileMapper;
import com.odiaz.moneymanager.model.ProfileEntity;
import com.odiaz.moneymanager.security.JwtAuthFilter;
import com.odiaz.moneymanager.service.ProfileService;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping
public class ProfileController {

    private final ProfileService profileService;
    private final ProfileMapper profileMapper;

    public ProfileController(ProfileService profileService, ProfileMapper profileMapper) {
        this.profileService = profileService;
        this.profileMapper = profileMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody ProfileDTO body){
        ProfileDTO registered = profileService.registerProfile(body);
        return ResponseEntity.ok().body(registered);
    }

    @GetMapping("/activate")
    public ResponseEntity<?> activateProfile(@RequestParam(name = "token", defaultValue = "") String token ){
        boolean isActivated = profileService.activateProfile(token);
        if(isActivated){
            return ResponseEntity.ok().body(Map.of("message", "Profile activated successfully"));
        }else{
            return ResponseEntity.badRequest().body(Map.of("message", "Activation token not found or already used"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDTO authDTO){
        try{
            Map<String, Object> resp = profileService.authAndGenerateToken(authDTO);
            return ResponseEntity.ok().body(resp);

        }catch(BadCredentialsException e){
            System.out.println(e);
            return ResponseEntity.badRequest().body(Map.of("message","Credenciales inválidas"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message",e.getMessage()));
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> userLogged(){
        ProfileEntity profile = profileService.getUserLogged();
        ProfileDTO resp = profileMapper.toDTO(profile);

        return ResponseEntity.ok().body(resp);
    }


}
