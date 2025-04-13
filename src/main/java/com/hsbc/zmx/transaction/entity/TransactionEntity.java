package com.hsbc.zmx.transaction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "transaction", indexes = { @Index(name = "idx_transaction_id", columnList = "transaction_id") })
@Setter
@Getter
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private Long id;

    @Column(name = "create_time", updatable = false)
    @CreationTimestamp
    private OffsetDateTime createTime;

    @Column(name = "modify_time")
    @UpdateTimestamp
    private OffsetDateTime modifyTime;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "transaction_id", unique = true)
    private String transactionId;

    @Column(name = "type")
    private String type;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "transaction_amount")
    private BigDecimal transactionAmount;

    @Column(name = "service_charge")
    private BigDecimal serviceCharge;

    @Column(name = "status")
    private String status;

    @Column(name = "peer_account_number")
    private String peerAccountNumber;

    @Column(name = "peer_account_name")
    private String peerAccountName;

    @Column(name = "version")
    @Version
    private Integer version;
}
