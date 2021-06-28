package com.github.ashimjk.hazelcast.service;

import com.github.ashimjk.hazelcast.model.Transaction;
import com.github.ashimjk.hazelcast.model.TransactionAmountAggregator;
import com.github.ashimjk.hazelcast.shared.StoreNames;
import com.hazelcast.aggregation.Aggregators;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReportingService implements StoreNames {

    private IMap<Long, Transaction> transactionById;
    private final HazelcastInstance clientInstance;

    @PostConstruct
    public void init() {
        transactionById = clientInstance.getMap(TRANSACTIONS_MAP);
    }

    public BigDecimal getIncome(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        TransactionAmountAggregator aggregator = new TransactionAmountAggregator(startDateTime, endDateTime);
        return transactionById.aggregate(aggregator);
    }

    public BigDecimal sumOfTransaction(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Predicate<Long, Transaction> startPredicate = Predicates.greaterEqual("transactionDateTime", startDateTime);
        Predicate<Long, Transaction> endPredicate = Predicates.lessThan("transactionDateTime", endDateTime);
        Predicate<Long, Transaction> andPredicate = Predicates.and(startPredicate, endPredicate);

        return transactionById.aggregate(Aggregators.bigDecimalSum(), andPredicate);
    }

}
