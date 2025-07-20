package com.leafpay.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.leafpay.domain.AlerteSecurite} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlerteSecuriteDTO implements Serializable {

    private Long id;

    @Size(max = 100)
    private String typeAlerte;

    @Size(max = 50)
    private String niveauSeverite;

    private Instant timestamp;

    private Boolean estTraitee;

    private UtilisateurDTO utilisateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeAlerte() {
        return typeAlerte;
    }

    public void setTypeAlerte(String typeAlerte) {
        this.typeAlerte = typeAlerte;
    }

    public String getNiveauSeverite() {
        return niveauSeverite;
    }

    public void setNiveauSeverite(String niveauSeverite) {
        this.niveauSeverite = niveauSeverite;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getEstTraitee() {
        return estTraitee;
    }

    public void setEstTraitee(Boolean estTraitee) {
        this.estTraitee = estTraitee;
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
        if (!(o instanceof AlerteSecuriteDTO)) {
            return false;
        }

        AlerteSecuriteDTO alerteSecuriteDTO = (AlerteSecuriteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alerteSecuriteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlerteSecuriteDTO{" +
            "id=" + getId() +
            ", typeAlerte='" + getTypeAlerte() + "'" +
            ", niveauSeverite='" + getNiveauSeverite() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            ", estTraitee='" + getEstTraitee() + "'" +
            ", utilisateur=" + getUtilisateur() +
            "}";
    }
}
