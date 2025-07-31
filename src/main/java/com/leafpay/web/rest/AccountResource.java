package com.leafpay.web.rest;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.leafpay.security.SecurityUtils;
import com.leafpay.service.UtilisateurService;
import com.leafpay.service.dto.UtilisateurDTO;

import java.security.Principal;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AccountResource {

    private static final Logger LOG = LoggerFactory.getLogger(AccountResource.class);

    private final UtilisateurService utilisateurService;

    public AccountResource(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    /**
     * {@code GET  /account} : get the current user.
     *
     * @param principal the current user; resolves to {@code null} if not
     *                  authenticated.
     * @return the current user.
     * @throws AccountResourceException {@code 500 (Internal Server Error)} if the
     *                                  user couldn't be returned.
     */
    @GetMapping("/account")
    public UtilisateurDTO getAccount(Principal principal) {
        if (principal instanceof AbstractAuthenticationToken) {
            String email = principal.getName();
            return utilisateurService.findOneByEmail(email)
                    .orElseThrow(() -> new AccountResourceException("User could not be found"));
        } else {
            throw new AccountResourceException("User could not be found");
        }
    }

    private static class AccountResourceException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public AccountResourceException(String message) {
            super(message);
        }
    }

    private static class UserVM {

        private String login;
        private Set<String> authorities;
        private Map<String, Object> details;

        UserVM(String login, Set<String> authorities, Map<String, Object> details) {
            this.login = login;
            this.authorities = authorities;
            this.details = details;
        }

        public boolean isActivated() {
            return true;
        }

        public Set<String> getAuthorities() {
            return authorities;
        }

        public String getLogin() {
            return login;
        }

        @JsonAnyGetter
        public Map<String, Object> getDetails() {
            return details;
        }
    }

    private static UserVM getUserFromAuthentication(AbstractAuthenticationToken authToken) {
        Map<String, Object> attributes;
        if (authToken instanceof JwtAuthenticationToken) {
            attributes = ((JwtAuthenticationToken) authToken).getTokenAttributes();
        } else {
            throw new IllegalArgumentException("AuthenticationToken is not OAuth2 or JWT!");
        }

        return new UserVM(
                authToken.getName(),
                authToken.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()),
                SecurityUtils.extractDetailsFromTokenAttributes(attributes));
    }
}
