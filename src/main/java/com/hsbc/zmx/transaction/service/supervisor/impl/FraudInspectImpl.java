package com.hsbc.zmx.transaction.service.supervisor.impl;

import com.hsbc.zmx.transaction.entity.TransactionEntity;
import com.hsbc.zmx.transaction.service.supervisor.SuperviseInterface;
import com.hsbc.zmx.transaction.service.supervisor.SupervisionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class FraudInspectImpl implements SuperviseInterface {
    @Override
    public SupervisionResult supervise(TransactionEntity entity) {
        log.info("supervise: {}", this.getClass().getSimpleName());

        //check fraud

        //example check
        if (entity.getTransactionAmount().compareTo(new BigDecimal("10000000")) > 0) {
            return new SupervisionResult(false, "fake", "400");
        }
        return SupervisionResult.success();
    }

    @Override
    public int getPriority() {
        //highest priority
        return 0;
    }
}
