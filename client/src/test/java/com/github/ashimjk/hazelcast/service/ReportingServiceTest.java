package com.github.ashimjk.hazelcast.service;

import com.github.ashimjk.hazelcast.ClientApplication;
import com.github.ashimjk.hazelcast.config.ClientTestConfiguration;
import com.github.ashimjk.hazelcast.model.Transaction;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.github.ashimjk.hazelcast.service.MapNames.TRANSACTIONS_MAP;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {"spring.main.allow-bean-definition-overriding=true"},
        classes = {ClientApplication.class, ClientTestConfiguration.class})
class ReportingServiceTest {

    @Autowired private ReportingService reportingService;
    @Autowired private HazelcastInstance clientInstance;

    @BeforeEach
    public void tearDown() {
        clientInstance.getMap(ReportingService.TRANSACTIONS_MAP).clear();
    }

    @Test
    public void testGetIncome() {
        generateTransactions(100);

        LocalDateTime startDateTime = LocalDateTime.now().plusYears(1);
        LocalDateTime endDateTime = LocalDateTime.now().plusYears(5);

        BigDecimal income = reportingService.getIncome(startDateTime, endDateTime);

        assertThat(income).isEqualByComparingTo(BigDecimal.valueOf(4L));
    }

    public void generateTransactions(int maxTransactions) {
        Long customerId = 1L;
        Map<Long, Transaction> genTransactions = new HashMap<>();

        for (long x = 1; x <= maxTransactions; x++) {
            Transaction transaction = new Transaction(x,
                                                      customerId,
                                                      LocalDateTime.now().plusYears(x),
                                                      BigDecimal.ONE);
            genTransactions.put(x, transaction);
        }

        IMap<Object, Object> transactionsById = clientInstance.getMap(TRANSACTIONS_MAP);
        transactionsById.putAll(genTransactions);
    }

}