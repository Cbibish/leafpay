package com.leafpay.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.leafpay.domain.Log} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LogDTO implements Serializable {

    private Long id;

    @Size(max = 255)
    private String action;

    private Instant timestamp;

    @Size(max = 50)
    private String ipUtilisateur;

    @Size(max = 50)
    private String resultat;

    @Lob
    private String description;

    private UtilisateurDTO utilisateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getIpUtilisateur() {
        return ipUtilisateur;
    }

    public void setIpUtilisateur(String ipUtilisateur) {
        this.ipUtilisateur = ipUtilisateur;
    }

    public String getResultat() {
        return resultat;
    }

    public void setResultat(String resultat) {
        this.resultat = resultat;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UtilisateurDTO getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(UtilisateurDTO utilisateur) {
        this.utilisateur = utilisateur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LogDTO)) {
            return false;
        }

        LogDTO logDTO = (LogDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, logDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LogDTO{" +
            "id=" + getId() +
            ", action='" + getAction() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            ", ipUtilisateur='" + getIpUtilisateur() + "'" +
            ", resultat='" + getResultat() + "'" +
            ", description='" + getDescription() + "'" +
            ", utilisateur=" + getUtilisateur() +
            "}";
    }
}
