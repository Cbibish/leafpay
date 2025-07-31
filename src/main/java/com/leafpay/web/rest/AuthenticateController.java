package com.leafpay.web.rest;

import static com.leafpay.security.SecurityUtils.AUTHORITIES_CLAIM;
import static com.leafpay.security.SecurityUtils.JWT_ALGORITHM;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leafpay.service.AlerteSecuriteService;
import com.leafpay.service.LogService;
import com.leafpay.service.dto.AlerteSecuriteDTO;
import com.leafpay.service.dto.LogDTO;
import com.leafpay.service.enums.UserRole;
import com.leafpay.web.rest.vm.LoginVM;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class AuthenticateController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticateController.class);

    private final JwtEncoder jwtEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final LogService logService;
    private final AlerteSecuriteService alerteSecuriteService; // NEW

    @Value("${jhipster.security.authentication.jwt.token-validity-in-seconds:0}")
    private long tokenValidityInSeconds;

    @Value("${jhipster.security.authentication.jwt.token-validity-in-seconds-for-remember-me:0}")
    private long tokenValidityInSecondsForRememberMe;

    private final ConcurrentHashMap<String, AtomicInteger> failedAttempts = new ConcurrentHashMap<>();

    public AuthenticateController(
        JwtEncoder jwtEncoder,
        AuthenticationManagerBuilder authenticationManagerBuilder,
        LogService logService,
        AlerteSecuriteService alerteSecuriteService // Inject
    ) {
        this.jwtEncoder = jwtEncoder;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.logService = logService;
        this.alerteSecuriteService = alerteSecuriteService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {
        String username = loginVM.getUsername();
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(username, loginVM.getPassword());

        Authentication authentication = null;
        String resultat = "FAILURE";
        String description = "Invalid credentials";

        try {
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            resultat = "SUCCESS";
            description = "User logged in successfully";

            String jwt = this.createToken(authentication, loginVM.isRememberMe());

            // Reset failed attempts on success
            failedAttempts.remove(username);

            // Log success
            createLog("LOGIN", resultat, description, username);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBearerAuth(jwt);
            return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);

        } catch (Exception e) {
            // Increment failed attempts
            int attempts = failedAttempts.computeIfAbsent(username, k -> new AtomicInteger(0)).incrementAndGet();

            // Log failure
            createLog("LOGIN", resultat, description, username);

            // If 3 or more failed attempts, create SECURITY ALERT in AlerteSecurite table
            if (attempts >= 3) {
                createSecurityAlert(username);
            }

            throw e;
        }
    }

    private void createLog(String action, String resultat, String description, String username) {
        try {
            LogDTO logDTO = new LogDTO();
            logDTO.setAction(action);
            logDTO.setResultat(resultat);
            logDTO.setDescription(description);
            logDTO.setTimestamp(Instant.now());
            logDTO.setIpUtilisateur("UNKNOWN"); // Or fetch from request
            // You can also set utilisateur if available
            logService.save(logDTO);
        } catch (Exception e) {
            LOG.error("Failed to create log for user: {}", username, e);
        }
    }

    private void createSecurityAlert(String username) {
        try {
            AlerteSecuriteDTO alert = new AlerteSecuriteDTO();
            alert.setTypeAlerte("MULTIPLE_FAILED_LOGINS");
            alert.setNiveauSeverite("LOW");
            alert.setTimestamp(Instant.now());
            alert.setEstTraitee(false);
            // Optionally link UtilisateurDTO if available
            alerteSecuriteService.save(alert);
            LOG.warn("Security alert created for user: {}", username);
        } catch (Exception e) {
            LOG.error("Failed to create security alert for user: {}", username, e);
        }
    }

    /**
     * {@code GET /authenticate} : check if the user is authenticated.
     */
    @GetMapping("/authenticate")
    public ResponseEntity<Void> isAuthenticated(Principal principal) {
        LOG.debug("REST request to check if the current user is authenticated");
        return ResponseEntity.status(principal == null ? HttpStatus.UNAUTHORIZED : HttpStatus.NO_CONTENT).build();
    }

    public String createToken(Authentication authentication, boolean rememberMe) {
        Instant now = Instant.now();
        Instant validity = rememberMe
                ? now.plus(this.tokenValidityInSecondsForRememberMe, ChronoUnit.SECONDS)
                : now.plus(this.tokenValidityInSeconds, ChronoUnit.SECONDS);

        String roleName = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(r -> !r.isEmpty())
                .findFirst()
                .orElse("NORMAL_USER");

        UserRole roleEnum;
        try {
            roleEnum = UserRole.valueOf(roleName);
        } catch (IllegalArgumentException e) {
            roleEnum = UserRole.NORMAL_USER;
        }

        JwtClaimsSet.Builder builder = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(authentication.getName())
                .claim(AUTHORITIES_CLAIM, roleName)
                .claim("role", roleEnum.name());

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, builder.build())).getTokenValue();
    }


    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {
        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}
