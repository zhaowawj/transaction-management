package com.hsbc.zmx.transaction.repository;

import com.hsbc.zmx.transaction.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends CrudRepository<TransactionEntity, UUID>, JpaRepository<TransactionEntity, UUID>, JpaSpecificationExecutor<TransactionEntity> {

    Optional<TransactionEntity> findByTransactionId(String transactionId);

    Integer deleteByTransactionId(String transactionId);

    Optional<TransactionEntity> findByClientIdAndTransactionId(String clientId, String transactionId);

    Integer deleteByClientIdAndTransactionId(String clientId, String transactionId);
}
