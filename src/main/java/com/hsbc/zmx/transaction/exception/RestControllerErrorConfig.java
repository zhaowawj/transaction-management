package com.hsbc.zmx.transaction.exception;

import com.hsbc.zmx.transaction.constant.ApiErrorEnum;
import com.hsbc.zmx.transaction.controller.response.ApiBaseResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class RestControllerErrorConfig {

    //TODO ADD OTHER EXCEPTION HANDLER

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiBaseResponse<Void> handleConstraintViolationException(ConstraintViolationException e) {
        StringBuilder sb = new StringBuilder();
        for (ConstraintViolation violation : e.getConstraintViolations()) {
            sb.append(violation.getMessage());
        }
        return new ApiBaseResponse<>(ApiErrorEnum.API_40000.getErrCode(), sb.toString());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiBaseResponse<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuilder sb = new StringBuilder();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            sb.append(fieldError.getDefaultMessage());
        }
        return new ApiBaseResponse<>(ApiErrorEnum.API_40000.getErrCode(), sb.toString());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiBaseResponse<Void>> handleAllExceptions(Exception ex) {
        log.error("Exception occurred", ex);
        return new ResponseEntity<>(new ApiBaseResponse<>(ApiErrorEnum.API_50000), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
