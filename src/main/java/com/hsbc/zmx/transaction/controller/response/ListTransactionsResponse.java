package com.hsbc.zmx.transaction.controller.response;

import com.hsbc.zmx.transaction.dto.TransactionModel;
import lombok.Data;

import java.util.List;

@Data
public class ListTransactionsResponse {

    List<TransactionModel> transactions;

    Integer pageIndex;

    Integer pageSize;

    Long totalCount;

    Integer totalPage;
}
