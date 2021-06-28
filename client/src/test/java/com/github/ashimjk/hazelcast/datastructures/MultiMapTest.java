package com.github.ashimjk.hazelcast.datastructures;

import com.github.ashimjk.hazelcast.ClientApplication;
import com.github.ashimjk.hazelcast.config.ClientTestConfiguration;
import com.github.ashimjk.hazelcast.domain.Customer;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.multimap.MultiMap;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = {"spring.main.allow-bean-definition-overriding=true"},
        classes = {ClientApplication.class, ClientTestConfiguration.class})
public class MultiMapTest {

    @Autowired private HazelcastInstance clientInstance;

    @Test
    public void multiMapExample() {
        Customer customer1 = new Customer(1L, "Grant Little", null, "grant@grantlittle.me");
        Customer customer2 = new Customer(2L, "Simon", null, "simon@somecompany.com");
        Customer customer3 = new Customer(3L, "Jane", null, "jane@somecompany.com");

        MultiMap<String, Customer> accountToCustomersMap = clientInstance.getMultiMap("account-to-customers");
        accountToCustomersMap.put("1", customer1);
        accountToCustomersMap.put("2", customer2);
        accountToCustomersMap.put("2", customer3);

        assertEquals(1, accountToCustomersMap.get("1").size());
        assertEquals(2, accountToCustomersMap.get("2").size());
    }

}