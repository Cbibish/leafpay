package com.leafpay.service.dto;

import java.math.BigDecimal;

import jakarta.persistence.Column;

public class TransferRequestDTO {

    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal montant;

    @Column(name = "justificatif", nullable = true)
    private String justificatif;

    @Column(name = "moyen_validation", nullable = true)
    private String moyenValidation;

    public TransferRequestDTO() {
    }

    public TransferRequestDTO(Long fromAccountId, Long toAccountId, BigDecimal montant) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.montant = montant;
    }

    public Long getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(Long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public Long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(Long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public BigDecimal getAmount() {
        return montant;
    }

    public void setAmount(BigDecimal montant) {
        this.montant = montant;
    }

    public String getJustificatif() {
        return justificatif;
    }

    public void setJustificatif(String justificatif) {
        this.justificatif = justificatif;
    }

    public String getMoyenValidation() {
        return moyenValidation;
    }

    public void setMoyenValidation(String moyenValidation) {
        this.moyenValidation = moyenValidation;
    }

    @Override
    public String toString() {
        return "TransferRequestDTO{" +
                "fromAccountId=" + fromAccountId +
                ", toAccountId=" + toAccountId +
                ", montant=" + montant +
                '}';
    }
}
