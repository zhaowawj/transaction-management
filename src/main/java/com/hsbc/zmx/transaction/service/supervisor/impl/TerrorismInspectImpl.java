package com.hsbc.zmx.transaction.service.supervisor.impl;

import com.hsbc.zmx.transaction.entity.TransactionEntity;
import com.hsbc.zmx.transaction.service.supervisor.SuperviseInterface;
import com.hsbc.zmx.transaction.service.supervisor.SupervisionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TerrorismInspectImpl implements SuperviseInterface {
    public SupervisionResult supervise(TransactionEntity transaction) {
        log.info("supervise: {}", this.getClass().getSimpleName());
        //TODO: implement
        return SupervisionResult.success();
    }
}
