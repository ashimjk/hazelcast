package com.github.ashimjk.hazelcast.datastructures;

import com.github.ashimjk.hazelcast.ClientApplication;
import com.github.ashimjk.hazelcast.config.ClientTestConfiguration;
import com.github.ashimjk.hazelcast.domain.Customer;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.multimap.MultiMap;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static com.github.ashimjk.hazelcast.shared.StoreNames.ACCOUNT_TO_CUSTOMERS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = {"spring.main.allow-bean-definition-overriding=true"},
        classes = {ClientApplication.class, ClientTestConfiguration.class})
public class MultiMapTest {

    @Autowired private HazelcastInstance clientInstance;

    @Test
    public void multiMapExample() {
        Customer customer1 = new Customer(1L, "Ashim Khadka", LocalDate.now(), "ashim@gmail.com");
        Customer customer2 = new Customer(2L, "Kushal Sherchan", LocalDate.now(), "kushal@gmail.com");
        Customer customer3 = new Customer(3L, "Shekhar Rai", LocalDate.now(), "shekar@gmail.com");

        MultiMap<String, Customer> accountToCustomersMap = clientInstance.getMultiMap(ACCOUNT_TO_CUSTOMERS);
        accountToCustomersMap.put("1", customer1);
        accountToCustomersMap.put("2", customer2);
        accountToCustomersMap.put("2", customer3);

        assertEquals(1, accountToCustomersMap.get("1").size());
        assertEquals(2, accountToCustomersMap.get("2").size());
    }

}