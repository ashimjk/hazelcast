package com.github.ashimjk.hazelcast.datastructures;

import com.github.ashimjk.hazelcast.ClientApplication;
import com.github.ashimjk.hazelcast.config.ClientTestConfiguration;
import com.github.ashimjk.hazelcast.domain.Email;
import com.github.ashimjk.hazelcast.service.StorageNodeFactory;
import com.hazelcast.collection.IQueue;
import com.hazelcast.core.HazelcastInstance;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.github.ashimjk.hazelcast.shared.StoreNames.EMAIL_QUEUE;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = {"spring.main.allow-bean-definition-overriding=true"},
        classes = {ClientApplication.class, ClientTestConfiguration.class})
public class QueueTest {

    @Autowired private HazelcastInstance clientInstance;
    @Autowired private StorageNodeFactory storageNodeFactory;

    private IQueue<Email> emailQueue = null;

    @AfterEach
    public void tearDown() {
        emailQueue.clear();
    }

    @Test
    public void testQueuePersistence() throws Exception {

        storageNodeFactory.ensureClusterSize(2);

        emailQueue = clientInstance.getQueue(EMAIL_QUEUE);

        Email email1 = new Email(UUID.randomUUID().toString(), "address1", "subject1", "body1");
        Email email2 = new Email(UUID.randomUUID().toString(), "address2", "subject2", "body2");
        Email email3 = new Email(UUID.randomUUID().toString(), "address3", "subject3", "body3");

        List<Email> emails = new ArrayList<>(3);
        emails.add(email1);
        emails.add(email2);
        emails.add(email3);

        emailQueue.addAll(emails);

        Email emailFromQueue1 = emailQueue.poll();
        assertEquals(email1, emailFromQueue1);

        storageNodeFactory.ensureClusterSize(0);

        storageNodeFactory.ensureClusterSize(2);

        Email emailFromQueue2 = emailQueue.poll();
        assertEquals(email2, emailFromQueue2);

        Email emailFromQueue3 = emailQueue.poll();
        assertEquals(email3, emailFromQueue3);

        assertEquals(0, emailQueue.size());
    }

}