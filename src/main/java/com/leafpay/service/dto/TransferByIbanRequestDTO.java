package com.leafpay.service.dto;

import java.math.BigDecimal;

public class TransferByIbanRequestDTO {
    private String fromIban;
    private String toIban;
    private BigDecimal amount;
    private String justificatif;
    private String moyenValidation;

    // Getters and setters
    public String getFromIban() {
        return fromIban;
    }
    public void setFromIban(String fromIban) {
        this.fromIban = fromIban;
    }
    public String getToIban() {
        return toIban;
    }
    public void setToIban(String toIban) {
        this.toIban = toIban;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
}
