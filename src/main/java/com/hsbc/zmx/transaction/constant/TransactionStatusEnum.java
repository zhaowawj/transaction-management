package com.hsbc.zmx.transaction.constant;

import lombok.Getter;


@Getter
public enum TransactionStatusEnum {
    TRANSACTION_STATUS_CREATED("CREATED", "CREATED"),
    TRANSACTION_STATUS_PROCESSING("PROCESSING", "PROCESSING"),
    TRANSACTION_STATUS_COMPLETED("COMPLETED", "COMPLETED"),
    TRANSACTION_STATUS_FAILED("FAILED", "FAILED"),
    TRANSACTION_STATUS_CANCELLED("CANCELLED", "CANCELLED");

    private String status;
    private String statusDesc;

    TransactionStatusEnum(String status, String statusDesc) {
        this.status = status;
        this.statusDesc = statusDesc;
    }
}
