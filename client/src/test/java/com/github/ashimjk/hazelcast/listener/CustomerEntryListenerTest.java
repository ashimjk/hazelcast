package com.github.ashimjk.hazelcast.listener;

import com.github.ashimjk.hazelcast.ClientApplication;
import com.github.ashimjk.hazelcast.config.ClientTestConfiguration;
import com.github.ashimjk.hazelcast.domain.Customer;
import com.github.ashimjk.hazelcast.domain.Email;
import com.github.ashimjk.hazelcast.shared.StoreNames;
import com.hazelcast.collection.IQueue;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = {"spring.main.allow-bean-definition-overriding=true"},
        classes = {ClientApplication.class, ClientTestConfiguration.class})
public class CustomerEntryListenerTest {

    @Autowired private HazelcastInstance clientInstance;

    @AfterEach
    public void tearDown() throws Exception {
        clientInstance.getMap(StoreNames.CUSTOMERS_MAP).clear();
        Thread.sleep(300L);
        clientInstance.getQueue(StoreNames.EMAIL_QUEUE).clear();
    }

    @Test
    public void testEntryAdded() throws Exception {
        IMap<Long, Customer> customersMap = clientInstance.getMap(StoreNames.CUSTOMERS_MAP);
        Customer customer = new Customer(1L, "Ashim Khadka", LocalDate.now(), "ashim@gmail.com");
        customersMap.put(1L, customer);

        IQueue<Email> emailQueue = clientInstance.getQueue(StoreNames.EMAIL_QUEUE);
        Email email = emailQueue.take();
        assertNotNull(email);
        assertEquals("ashim@gmail.com", email.getToAddress());

        String expectedBody = "Hi " + customer.getName() + ", Thanks for joining our Hazelcast Store.";
        assertEquals(expectedBody, email.getBody());
    }

    @Test
    public void testEntryUpdated() throws Exception {
        IMap<Long, Customer> customersMap = clientInstance.getMap(StoreNames.CUSTOMERS_MAP);
        Customer customer = new Customer(1L, "Ashim Khadka", LocalDate.now(), "wrong@gmail.com");
        customersMap.put(1L, customer);

        IQueue<Email> emailQueue = clientInstance.getQueue(StoreNames.EMAIL_QUEUE);
        //Take the customer added email off the queue
        emailQueue.take();

        //Update the customer
        customer = new Customer(1L, "Ashim Khadka", LocalDate.now(), "ashim@gmail.com");
        customersMap.put(1L, customer);

        Email email = emailQueue.take();
        assertNotNull(email);
        assertEquals("ashim@gmail.com", email.getToAddress());

        String expectedBody = "Hi " + customer.getName() + ", We're just letting you know that we have updated your details.";
        assertEquals(expectedBody, email.getBody());
    }

    @Test
    public void testEntryRemoved() throws Exception {
        IMap<Long, Customer> customersMap = clientInstance.getMap(StoreNames.CUSTOMERS_MAP);
        Customer customer = new Customer(1L, "Ashim Khadka", LocalDate.now(), "ashim@gmail.com");
        customersMap.put(1L, customer);

        IQueue<Email> emailQueue = clientInstance.getQueue(StoreNames.EMAIL_QUEUE);
        //Take the customer added email off the queue
        emailQueue.take();

        //Remove the customers
        customersMap.remove(1L);

        Email email = emailQueue.take();
        assertNotNull(email);
        assertEquals("ashim@gmail.com", email.getToAddress());

        String expectedBody = "Hi " + customer.getName() + ", We're sorry to see you leave us.";
        assertEquals(expectedBody, email.getBody());
    }

}