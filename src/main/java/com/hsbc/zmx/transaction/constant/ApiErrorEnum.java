package com.hsbc.zmx.transaction.constant;

import lombok.Getter;

@Getter
public enum ApiErrorEnum {
    API_SUCCESS("API-20000", "Success"),

    API_40000("API-40000", "Api parameter invalid"),
    API_40001("API-40001", "Illegal request"),
    API_40400("API-40400", "Content not found"),
    API_40900("API-40900", "Duplicate"),
    API_50000("API-50000", "Server error"),

    API_DB_OPERATION_EXCEPTION("API-50003", "Database operation exception.");


    private String errCode;
    private String errMsg;

    public String getI18nErrMsg() {
        //TODO i18n
        return errMsg;
    }

    ApiErrorEnum(String errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }
}