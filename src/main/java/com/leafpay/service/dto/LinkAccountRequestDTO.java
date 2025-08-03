package com.leafpay.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LinkAccountRequestDTO {

    @NotNull(message = "IBAN must not be null")
    @Size(min = 15, max = 34, message = "IBAN must be between 15 and 34 characters")
    private String iban;

    @NotNull(message = "User ID must not be null")
    private Long userId;

    public LinkAccountRequestDTO() {
        // Default constructor
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
