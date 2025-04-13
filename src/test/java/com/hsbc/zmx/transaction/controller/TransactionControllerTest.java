package com.hsbc.zmx.transaction.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hsbc.zmx.transaction.constant.ApiErrorEnum;
import com.hsbc.zmx.transaction.constant.TransactionStatusEnum;
import com.hsbc.zmx.transaction.controller.response.ApiBaseResponse;
import com.hsbc.zmx.transaction.controller.response.ListTransactionsResponse;
import com.hsbc.zmx.transaction.dto.TransactionModel;
import com.hsbc.zmx.transaction.util.JsonUtil;
import com.hsbc.zmx.transaction.util.ResourceUtil;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;

@AutoConfigureMockMvc
@SpringBootTest
class TransactionControllerTest {

    @Inject
    private MockMvc mockMvc;

    private String transactionIdInit = "TXN000001";

    @BeforeEach
    void setUp() throws Exception {
        String request = ResourceUtil.readFileContent("requests/PredefineTransaction.json");
        final String result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/transactions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        ApiBaseResponse<Void> response = JsonUtil.jsonToObject(result, ApiBaseResponse.class);
        Assertions.assertEquals(ApiErrorEnum.API_SUCCESS.getErrCode(), response.getErrCode());
    }

    @AfterEach
    void tearDown() throws Exception {
        final String result = mockMvc.perform(
                        MockMvcRequestBuilders.delete("/transactions/" + transactionIdInit)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

    }

    @Test
    void createTransaction() throws Exception {

        String request = ResourceUtil.readFileContent("requests/ValidTransaction.json");
        final String result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/transactions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        ApiBaseResponse<Void> response = JsonUtil.jsonToObject(result, ApiBaseResponse.class);
        Assertions.assertEquals(ApiErrorEnum.API_SUCCESS.getErrCode(), response.getErrCode());
    }


    @Test
    void createDuplicateTransaction() throws Exception {

        String request = ResourceUtil.readFileContent("requests/PredefineTransaction.json");
        final String result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/transactions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        ApiBaseResponse<Void> response = JsonUtil.jsonToObject(result, ApiBaseResponse.class);
        Assertions.assertEquals(ApiErrorEnum.API_40900.getErrCode(), response.getErrCode());
    }

    @Test
    void createInvalidTransaction() throws Exception {

        String request = ResourceUtil.readFileContent("requests/InvalidTransaction.json");
        final String result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/transactions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        ApiBaseResponse<Void> response = JsonUtil.jsonToObject(result, ApiBaseResponse.class);
        Assertions.assertEquals(ApiErrorEnum.API_40000.getErrCode(), response.getErrCode());
    }

    @Test
    void createIllegalTransaction() throws Exception {

        String request = ResourceUtil.readFileContent("requests/IllegalTransaction.json");
        final String result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/transactions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        ApiBaseResponse<Void> response = JsonUtil.jsonToObject(result, ApiBaseResponse.class);
        Assertions.assertEquals(ApiErrorEnum.API_40001.getErrCode(), response.getErrCode());
    }

    @Test
    void getTransaction() throws Exception {
        final String result = mockMvc.perform(
                        MockMvcRequestBuilders.get("/transactions/" + transactionIdInit)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        ApiBaseResponse<TransactionModel> response = JsonUtil.jsonToObject(result, ApiBaseResponse.class);
        Assertions.assertEquals(ApiErrorEnum.API_SUCCESS.getErrCode(), response.getErrCode());
    }

    @Test
    void getNoneExistedTransaction() throws Exception {
        final String result = mockMvc.perform(
                        MockMvcRequestBuilders.get("/transactions/" + "noneExisted")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        ApiBaseResponse<TransactionModel> response = JsonUtil.jsonToObject(result, ApiBaseResponse.class);
        Assertions.assertEquals(ApiErrorEnum.API_40400.getErrCode(), response.getErrCode());
    }

    @Test
    void listTransactions() throws Exception {
        final String result = mockMvc.perform(
                        MockMvcRequestBuilders.get("/transactions")
                                .param("pageIndex", "1")
                                .param("pageSize", "10")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        ApiBaseResponse<ListTransactionsResponse> response = JsonUtil.jsonToObject(result, ApiBaseResponse.class);
        Assertions.assertEquals(ApiErrorEnum.API_SUCCESS.getErrCode(), response.getErrCode());
    }

    @Test
    void updateTransaction() throws Exception {
        String request = ResourceUtil.readFileContent("requests/PredefineTransaction.json");
        final String result = mockMvc.perform(
                        MockMvcRequestBuilders.put("/transactions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        ApiBaseResponse<TransactionModel> response = JsonUtil.jsonToObject(result, new TypeReference<>() {
        });
        Assertions.assertEquals(ApiErrorEnum.API_SUCCESS.getErrCode(), response.getErrCode());
        Assertions.assertEquals(TransactionStatusEnum.TRANSACTION_STATUS_CREATED.getStatus(), response.getData().getStatus());
    }

    @Test
    void deleteTransaction() throws Exception {
        final String result = mockMvc.perform(
                        MockMvcRequestBuilders.delete("/transactions/" + transactionIdInit)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        ApiBaseResponse<Void> response = JsonUtil.jsonToObject(result, ApiBaseResponse.class);
        Assertions.assertEquals(ApiErrorEnum.API_SUCCESS.getErrCode(), response.getErrCode());
    }
}