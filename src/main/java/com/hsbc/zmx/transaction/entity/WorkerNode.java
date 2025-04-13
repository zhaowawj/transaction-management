package com.hsbc.zmx.transaction.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class WorkerNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    private String hostName;
    private String port;
    private int type;
    private Date launchDate;
    private Date modified;
    private Date created;
}