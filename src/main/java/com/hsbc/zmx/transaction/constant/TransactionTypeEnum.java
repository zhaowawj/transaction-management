package com.hsbc.zmx.transaction.constant;

import lombok.Getter;

@Getter
public enum TransactionTypeEnum {
    TRANSACTION_TYPE_DEPOSIT("DEPOSIT", "DEPOSIT"),
    TRANSACTION_TYPE_WITHDRAW("WITHDRAW", "WITHDRAW"),
    TRANSACTION_TYPE_TRANSFER("TRANSFER", "TRANSFER");

    private String type;
    private String typeDesc;

    TransactionTypeEnum(String type, String typeDesc) {
        this.type = type;
        this.typeDesc = typeDesc;
    }
}
