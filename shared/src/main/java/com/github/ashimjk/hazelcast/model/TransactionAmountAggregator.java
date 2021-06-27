package com.github.ashimjk.hazelcast.model;

import com.hazelcast.aggregation.Aggregator;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@RequiredArgsConstructor
public class TransactionAmountAggregator implements Aggregator<Map.Entry<Long, Transaction>, BigDecimal> {

    private static final long serialVersionUID = -4699736198827437405L;

    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;
    protected BigDecimal sum = BigDecimal.ZERO;

    @Override
    public void accumulate(Map.Entry<Long, Transaction> input) {
        Transaction transaction = input.getValue();
        LocalDateTime transactionDateTime = transaction.getTransactionDateTime();

        if (transactionDateTime.compareTo(startDateTime) >= 0
                && transactionDateTime.compareTo(endDateTime) < 0) {
            this.sum = sum.add(transaction.getAmount());
        }
    }

    @Override
    public void combine(Aggregator aggregator) {
        this.sum = sum.add(this.getClass().cast(aggregator).sum);
    }

    @Override
    public BigDecimal aggregate() {
        return sum;
    }

}
