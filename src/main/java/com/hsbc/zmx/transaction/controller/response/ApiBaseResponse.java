package com.hsbc.zmx.transaction.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsbc.zmx.transaction.constant.ApiErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiBaseResponse<T> {

    private String errCode = ApiErrorEnum.API_SUCCESS.getErrCode();

    private String errMsg = ApiErrorEnum.API_SUCCESS.getI18nErrMsg();

    private T data;

    public ApiBaseResponse(T data){
        this.data = data;
    }

    public ApiBaseResponse(ApiErrorEnum apiErrorEnum){
        this.errMsg = apiErrorEnum.getI18nErrMsg();
        this.errCode = apiErrorEnum.getErrCode();
    }

    public ApiBaseResponse(String errCode, String errMsg){
        this.errMsg = errMsg;
        this.errCode = errCode;
    }
}
