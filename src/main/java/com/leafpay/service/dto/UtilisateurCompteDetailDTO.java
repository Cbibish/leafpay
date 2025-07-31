package com.leafpay.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class UtilisateurCompteDetailDTO implements Serializable {

    // User details
    private Long utilisateurId;
    private String nom;
    private String prenom;
    private String email;

    // Account details
    private Long compteId;
    private String typeCompte;
    private BigDecimal solde;
    private String iban;

    // Role from bridging table
    private String roleUtilisateurSurCeCompte;

    public UtilisateurCompteDetailDTO(Long utilisateurId, String nom, String prenom, String email,
                                      Long compteId, String typeCompte, BigDecimal solde, String iban,
                                      String roleUtilisateurSurCeCompte) {
        this.utilisateurId = utilisateurId;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.compteId = compteId;
        this.typeCompte = typeCompte;
        this.solde = solde;
        this.iban = iban;
        this.roleUtilisateurSurCeCompte = roleUtilisateurSurCeCompte;
    }

    // Getters & setters
    public Long getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(Long utilisateurId) { this.utilisateurId = utilisateurId; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Long getCompteId() { return compteId; }
    public void setCompteId(Long compteId) { this.compteId = compteId; }

    public String getTypeCompte() { return typeCompte; }
    public void setTypeCompte(String typeCompte) { this.typeCompte = typeCompte; }

    public BigDecimal getSolde() { return solde; }
    public void setSolde(BigDecimal solde) { this.solde = solde; }

    public String getIban() { return iban; }
    public void setIban(String iban) { this.iban = iban; }

    public String getRoleUtilisateurSurCeCompte() { return roleUtilisateurSurCeCompte; }
    public void setRoleUtilisateurSurCeCompte(String roleUtilisateurSurCeCompte) { this.roleUtilisateurSurCeCompte = roleUtilisateurSurCeCompte; }
}
