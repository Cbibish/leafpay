package com.leafpay.service;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.leafpay.domain.Utilisateur;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UtilisateurService utilisateurService;

    public CustomUserDetailsService(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurService.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // get role name from the user
        String roleName = utilisateur.getRole() != null ? utilisateur.getRole().getNom() : "NORMAL_USER";

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + roleName));

        return new org.springframework.security.core.userdetails.User(
            utilisateur.getEmail(),
            utilisateur.getMotDePasse(),
            authorities
        );
    }
}

