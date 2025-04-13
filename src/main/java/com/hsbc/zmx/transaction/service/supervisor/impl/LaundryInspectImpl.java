package com.hsbc.zmx.transaction.service.supervisor.impl;

import com.hsbc.zmx.transaction.entity.TransactionEntity;
import com.hsbc.zmx.transaction.service.supervisor.SuperviseInterface;
import com.hsbc.zmx.transaction.service.supervisor.SupervisionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LaundryInspectImpl implements SuperviseInterface {
    @Override
    public SupervisionResult supervise(TransactionEntity transaction) {
        log.info("supervise: {}", this.getClass().getSimpleName());
        //todo
        return SupervisionResult.success();
    }
}
