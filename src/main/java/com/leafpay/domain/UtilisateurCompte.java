package com.leafpay.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UtilisateurCompte.
 */
@Entity
@Table(name = "utilisateur_compte")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UtilisateurCompte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 100)
    @Column(name = "role_utilisateur_sur_ce_compte", length = 100)
    private String roleUtilisateurSurCeCompte;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "idRole", "logs", "alerteSecurites" }, allowSetters = true)
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    private Compte compte;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UtilisateurCompte id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleUtilisateurSurCeCompte() {
        return this.roleUtilisateurSurCeCompte;
    }

    public UtilisateurCompte roleUtilisateurSurCeCompte(String roleUtilisateurSurCeCompte) {
        this.setRoleUtilisateurSurCeCompte(roleUtilisateurSurCeCompte);
        return this;
    }

    public void setRoleUtilisateurSurCeCompte(String roleUtilisateurSurCeCompte) {
        this.roleUtilisateurSurCeCompte = roleUtilisateurSurCeCompte;
    }

    public Utilisateur getUtilisateur() {
        return this.utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public UtilisateurCompte utilisateur(Utilisateur utilisateur) {
        this.setUtilisateur(utilisateur);
        return this;
    }

    public Compte getCompte() {
        return this.compte;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }

    public UtilisateurCompte compte(Compte compte) {
        this.setCompte(compte);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UtilisateurCompte)) {
            return false;
        }
        return getId() != null && getId().equals(((UtilisateurCompte) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UtilisateurCompte{" +
            "id=" + getId() +
            ", roleUtilisateurSurCeCompte='" + getRoleUtilisateurSurCeCompte() + "'" +
            "}";
    }
}
