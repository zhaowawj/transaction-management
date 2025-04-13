package com.hsbc.zmx.transaction.service;

import com.hsbc.zmx.transaction.constant.TransactionStatusEnum;
import com.hsbc.zmx.transaction.controller.request.ListTransactionsRequest;
import com.hsbc.zmx.transaction.controller.response.ListTransactionsResponse;
import com.hsbc.zmx.transaction.dto.TransactionModel;
import com.hsbc.zmx.transaction.entity.TransactionEntity;
import com.hsbc.zmx.transaction.repository.TransactionRepository;
import com.hsbc.zmx.transaction.service.supervisor.SuperviseService;
import com.hsbc.zmx.transaction.service.supervisor.SupervisionResult;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.hsbc.zmx.transaction.config.CacheableConfig.TRANSACTION_CACHE_KEY;
import static com.hsbc.zmx.transaction.config.CacheableConfig.TRANSACTION_CACHE_NAME;

@Service
@Transactional
@Slf4j
@CacheConfig(cacheNames = TRANSACTION_CACHE_NAME)
public class TransactionService {

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private SuperviseService superviseService;

    public SupervisionResult createTransaction(TransactionModel transaction) {
        TransactionEntity transactionEntity = convertDTOToEntity(transaction);

        SupervisionResult supervisionResult = superviseService.supervise(transactionEntity);
        if (supervisionResult.getSuccess()) {
            repository.save(transactionEntity);
        }
        return supervisionResult;
    }

    private static TransactionEntity convertDTOToEntity(TransactionModel transaction) {
        TransactionEntity transactionEntity = new TransactionEntity();

        transactionEntity.setTransactionId(transaction.getTransactionId());
        transactionEntity.setType(transaction.getType());
        transactionEntity.setClientId(transaction.getClientId());
        transactionEntity.setAccountNumber(transaction.getAccountNumber());
        transactionEntity.setTransactionAmount(transaction.getTransactionAmount());
        transactionEntity.setServiceCharge(transaction.getServiceCharge());
        transactionEntity.setPeerAccountNumber(transaction.getPeerAccountNumber());
        transactionEntity.setPeerAccountName(transaction.getPeerAccountName());
        transactionEntity.setVersion(transaction.getVersion());
        transactionEntity.setStatus(TransactionStatusEnum.TRANSACTION_STATUS_CREATED.getStatus());
        return transactionEntity;
    }

    @Cacheable(unless = "#result == null", key = "#p0")
    public Optional<TransactionModel> getCachedTransaction(String transactionId) {
        log.info("getCachedTransaction: transactionId={}", transactionId);
        return getTransaction(transactionId);
    }

    public Optional<TransactionModel> getTransaction(String transactionId) {

        Optional<TransactionEntity> transactionEntityOptional = repository.findByTransactionId(transactionId);
        if (transactionEntityOptional.isEmpty()) {
            return Optional.empty();
        }
        TransactionModel transactionModel = convertEntityToDTO(transactionEntityOptional.get());
        return Optional.of(transactionModel);
    }

    private static TransactionModel convertEntityToDTO(TransactionEntity transactionEntity) {
        TransactionModel transactionModel = new TransactionModel();
        transactionModel.setTransactionId(transactionEntity.getTransactionId());
        transactionModel.setType(transactionEntity.getType());
        transactionModel.setClientId(transactionEntity.getClientId());
        transactionModel.setAccountNumber(transactionEntity.getAccountNumber());
        transactionModel.setTransactionAmount(transactionEntity.getTransactionAmount());
        transactionModel.setServiceCharge(transactionEntity.getServiceCharge());
        transactionModel.setStatus(transactionEntity.getStatus());
        transactionModel.setPeerAccountNumber(transactionEntity.getPeerAccountNumber());
        transactionModel.setPeerAccountName(transactionEntity.getPeerAccountName());
        transactionModel.setVersion(transactionEntity.getVersion());
        return transactionModel;
    }

    @CacheEvict(key = "#p0")
    public void deleteTransaction(String transactionId) {
        repository.deleteByTransactionId(transactionId);
    }

    public ListTransactionsResponse listTransactions(ListTransactionsRequest request) {

        Specification<TransactionEntity> queryCondition = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (request.getClientId() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("clientId"), request.getClientId()));
            }
            if (request.getType() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("type"), request.getType()));
            }
            if (request.getTransactionId() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("transactionId"), request.getTransactionId()));
            }
            if (request.getCreateTimeStart() != null && request.getCreateTimeEnd() != null) {
                predicateList.add(criteriaBuilder.between(root.get("createTime"), request.getCreateTimeStart(), request.getCreateTimeEnd()));
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        };


        int pageNo = request.getPageIndex();
        int pageSize = request.getPageSize();
        Page<TransactionEntity> page = repository.findAll(queryCondition, PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Direction.DESC, "createTime")));

        ListTransactionsResponse result = new ListTransactionsResponse();
        result.setPageIndex(page.getNumber() + 1);
        result.setPageSize(page.getSize());
        result.setTotalCount(page.getTotalElements());
        result.setTotalPage(page.getTotalPages());
        List<TransactionModel> transactionModelList = new ArrayList<>();
        page.getContent().forEach(transactionEntity -> transactionModelList.add(convertEntityToDTO(transactionEntity)));
        result.setTransactions(transactionModelList);

        return result;
    }

    @CacheEvict(key = TRANSACTION_CACHE_KEY)
    public Optional<TransactionModel> updateTransaction(TransactionModel transaction) {
        Optional<TransactionEntity> optionalTransactionEntity = repository.findByTransactionId(transaction.getTransactionId());
        if (optionalTransactionEntity.isEmpty()) {
            return Optional.empty();
        }
        TransactionEntity entity = optionalTransactionEntity.get();
        // TODO: Which fields are supported for modification?
        entity.setStatus(transaction.getStatus());
        repository.saveAndFlush(entity);
        transaction.setVersion(entity.getVersion());
        return Optional.of(transaction);
    }
}
