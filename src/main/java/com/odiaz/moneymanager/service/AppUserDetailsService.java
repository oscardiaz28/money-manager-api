package com.odiaz.moneymanager.service;

import com.odiaz.moneymanager.model.ProfileEntity;
import com.odiaz.moneymanager.model.UserDetailsImpl;
import com.odiaz.moneymanager.repository.ProfileRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final ProfileRepository profileRepository;

    public AppUserDetailsService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ProfileEntity profile = profileRepository.findByEmail(email)
                .orElseThrow( () -> new UsernameNotFoundException("El usuario no ha sido encontrado") );

        return new UserDetailsImpl(profile);
    }

}
