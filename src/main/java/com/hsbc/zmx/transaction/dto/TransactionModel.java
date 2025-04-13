package com.hsbc.zmx.transaction.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionModel {

    @NotBlank(message = "Transaction ID is required.")
    private String transactionId;

    @NotBlank(message = "Transaction type is required.")
    private String type;

    @NotBlank(message = "Client ID is required.")
    private String clientId;

    @NotBlank(message = "Account number is required.")
    private String accountNumber;

    @DecimalMin(value = "0", inclusive = false, message = "Transaction amount must be greater than 0.")
    private BigDecimal transactionAmount;

    @Min(value = 0, message = "Service charge must be greater than or equal to 0.")
    private BigDecimal serviceCharge;

    private String status;

    private String peerAccountNumber;

    private String peerAccountName;

    private Integer version;
}
