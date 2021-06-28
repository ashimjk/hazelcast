package com.github.ashimjk.hazelcast.listeners;

import com.github.ashimjk.hazelcast.ClientApplication;
import com.github.ashimjk.hazelcast.config.ClientTestConfiguration;
import com.github.ashimjk.hazelcast.domain.Email;
import com.github.ashimjk.hazelcast.service.StorageNodeFactory;
import com.github.ashimjk.hazelcast.shared.StoreNames;
import com.hazelcast.core.HazelcastInstance;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"spring.main.allow-bean-definition-overriding=true"},
        classes = {ClientApplication.class, ClientTestConfiguration.class})
class EmailListenerTest {

    @Autowired private HazelcastInstance clientInstance;
    @Autowired private StorageNodeFactory storageNodeFactory;

    @AfterEach
    public void tearDown() {
        clientInstance.getQueue(StoreNames.EMAIL_QUEUE).clear();
    }

    @Test
    public void testLog() throws Exception {
        storageNodeFactory.ensureClusterSize(5);

        Email email = new Email("1234", "ashim@gmail.com", "subject", "body");

        clientInstance.getQueue(StoreNames.EMAIL_QUEUE).add(email);
    }

}