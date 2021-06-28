package com.github.ashimjk.hazelcast.datastructures;

import com.github.ashimjk.hazelcast.ClientApplication;
import com.github.ashimjk.hazelcast.config.ClientTestConfiguration;
import com.github.ashimjk.hazelcast.domain.Customer;
import com.github.ashimjk.hazelcast.service.StorageNodeFactory;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.topic.ITopic;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {"spring.main.allow-bean-definition-overriding=true"},
        classes = {ClientApplication.class, ClientTestConfiguration.class})
public class TopicTest {

    @Autowired private HazelcastInstance clientInstance;
    @Autowired private StorageNodeFactory storageNodeFactory;

    @Test
    public void topicExample() throws Exception {
        CountDownLatch latch = new CountDownLatch(2);

        storageNodeFactory.ensureClusterSize(1);

        ITopic<Customer> newCustomerTopic = clientInstance.getTopic("new-customer-topic");

        UUID messageListenerRef = newCustomerTopic.addMessageListener(message -> {
            // add business logic for message listener
            latch.countDown();
        });

        try {
            newCustomerTopic.publish(new Customer(1L, "Grant", LocalDate.now(), "grant@grantlittle.me"));
            newCustomerTopic.publish(new Customer(2L, "Simon", LocalDate.now(), "simon@somecompany.com"));

            assertTrue(latch.await(1, TimeUnit.SECONDS));

        } finally {
            newCustomerTopic.removeMessageListener(messageListenerRef);
        }

    }

}