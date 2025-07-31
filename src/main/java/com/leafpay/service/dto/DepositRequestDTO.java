// DepositRequestDTO.java
package com.leafpay.service.dto;

import java.math.BigDecimal;

public class DepositRequestDTO {
    private Long compteId;
    private BigDecimal montant;

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
}
