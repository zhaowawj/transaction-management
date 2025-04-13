package com.hsbc.zmx.transaction.controller;

import com.hsbc.zmx.transaction.constant.ApiErrorEnum;
import com.hsbc.zmx.transaction.controller.request.ListTransactionsRequest;
import com.hsbc.zmx.transaction.controller.response.ApiBaseResponse;
import com.hsbc.zmx.transaction.controller.response.ListTransactionsResponse;
import com.hsbc.zmx.transaction.dto.TransactionModel;
import com.hsbc.zmx.transaction.service.TransactionService;
import com.hsbc.zmx.transaction.service.supervisor.SupervisionResult;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/transactions")
@Slf4j
@Validated
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping
    public ApiBaseResponse<Void> createTransaction(@Valid @RequestBody TransactionModel request) {

        // check creating duplicate transaction
        Optional<TransactionModel> transactionDTO = transactionService.getTransaction(request.getTransactionId());
        if (transactionDTO.isPresent()) {
            return new ApiBaseResponse<>(ApiErrorEnum.API_40900);
        }

        SupervisionResult supervisionResult = transactionService.createTransaction(request);
        if(!supervisionResult.getSuccess()){
            return new ApiBaseResponse<>(ApiErrorEnum.API_40001);
        }
        // TODO: send transaction to kafka topic
        return new ApiBaseResponse<>();
    }

    @GetMapping("/{transactionId}")
    public ApiBaseResponse<TransactionModel> getTransaction(@NotBlank @PathVariable(value = "transactionId") String transactionId) {
        Optional<TransactionModel> transactionDTO = transactionService.getCachedTransaction(transactionId);
        if (transactionDTO.isEmpty()) {
            return new ApiBaseResponse<>(ApiErrorEnum.API_40400);
        }
        return new ApiBaseResponse<>(transactionDTO.get());
    }

    @GetMapping
    public ApiBaseResponse<ListTransactionsResponse> listTransactions(@Min(value = 1, message = "pageIndex must be greater than or equal to 1.") @RequestParam(required = false, value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                                      @Min(value = 1, message = "pageSize must be greater than or equal to 1.") @Max(value = 200, message = "pageSize must be smaller than or equal to 200.") @RequestParam(required = false, value = "pageSize", defaultValue = "10") Integer pageSize,
                                                                      @RequestParam(required = false, value = "clientId") String clientId,
                                                                      @RequestParam(required = false, value = "type") String type,
                                                                      @RequestParam(required = false, value = "transactionId") String transactionId,
                                                                      @RequestParam(required = false, value = "createTimeStart") String createTimeStart,
                                                                      @RequestParam(required = false, value = "createTimeEnd") String createTimeEnd) {

        ListTransactionsRequest request = new ListTransactionsRequest();
        request.setPageIndex(pageIndex);
        request.setPageSize(pageSize);
        request.setClientId(clientId);
        request.setType(type);
        request.setTransactionId(transactionId);
        if (StringUtils.isNotBlank(createTimeStart)) {
            request.setCreateTimeStart(OffsetDateTime.parse(createTimeStart));
        }
        if (StringUtils.isNotBlank(createTimeEnd)) {
            request.setCreateTimeEnd(OffsetDateTime.parse(createTimeEnd));
        }

        ListTransactionsResponse response = transactionService.listTransactions(request);


        return new ApiBaseResponse<>(response);
    }

    @PutMapping
    public ApiBaseResponse<TransactionModel> updateTransaction(@Valid @RequestBody TransactionModel request) {
        Optional<TransactionModel> transactionEntity = transactionService.updateTransaction(request);

        // TODO: send transaction to kafka topic

        return transactionEntity.map(ApiBaseResponse::new)
                .orElseGet(() -> new ApiBaseResponse<>(ApiErrorEnum.API_40400));

    }

    @DeleteMapping("/{transactionId}")
    public ApiBaseResponse<Void> deleteTransaction(@NotBlank @PathVariable(value = "transactionId") String transactionId) {
        Optional<TransactionModel> transactionDTO = transactionService.getTransaction(transactionId);
        if (transactionDTO.isEmpty()) {
            return new ApiBaseResponse<>(ApiErrorEnum.API_40400);
        }

        // 物理删除还是逻辑删除？
        transactionService.deleteTransaction(transactionId);

        // TODO: send   to kafka topic

        return new ApiBaseResponse<>();
    }


}
