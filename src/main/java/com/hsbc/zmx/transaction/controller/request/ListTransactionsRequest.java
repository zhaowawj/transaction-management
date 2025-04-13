package com.hsbc.zmx.transaction.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListTransactionsRequest {

    private String clientId;

    private String type;

    private String transactionId;

    private OffsetDateTime createTimeStart;

    private OffsetDateTime createTimeEnd;

    private Integer pageIndex;

    private Integer pageSize;
}
