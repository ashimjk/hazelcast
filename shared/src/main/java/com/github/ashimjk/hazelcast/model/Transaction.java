package com.github.ashimjk.hazelcast.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction implements Serializable {

    private static final long serialVersionUID = 6546457980655661321L;

    private Long transactionId;
    private Long customerId;
    private LocalDateTime transactionDateTime;
    private BigDecimal amount;

}
