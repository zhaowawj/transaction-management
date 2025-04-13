package com.hsbc.zmx.transaction.service.supervisor;

import com.hsbc.zmx.transaction.entity.TransactionEntity;


/**
 * Simple transaction supervision interface, support adding new supervision rule without the need to modify other implementation.
 */
public interface SuperviseInterface {

    SupervisionResult supervise(TransactionEntity transaction);

    default int getPriority() {
        return 100;
    }


}

