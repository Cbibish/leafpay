package com.leafpay.service.dto;

import java.math.BigDecimal;

public class MoneyOperationRequestDTO {

    private Long compteId;
    private BigDecimal montant;
    private String operationType; // "depot" or "retrait"

    public MoneyOperationRequestDTO() {
        // default constructor
    }

    public MoneyOperationRequestDTO(Long compteId, BigDecimal montant, String operationType) {
        this.compteId = compteId;
        this.montant = montant;
        this.operationType = operationType;
    }

    public Long getCompteId() {
        return compteId;
    }

    public void setCompteId(Long compteId) {
        this.compteId = compteId;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
}
