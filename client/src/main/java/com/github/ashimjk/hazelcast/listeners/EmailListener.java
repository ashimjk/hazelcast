package com.github.ashimjk.hazelcast.listeners;

import com.github.ashimjk.hazelcast.domain.Email;
import com.github.ashimjk.hazelcast.shared.StoreNames;
import com.hazelcast.collection.IQueue;
import com.hazelcast.collection.ItemEvent;
import com.hazelcast.collection.ItemListener;
import com.hazelcast.core.HazelcastInstance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailListener implements ItemListener<Email>, StoreNames {

    private final HazelcastInstance clientInstance;
    private UUID listenerId;

    @PostConstruct
    public void init() {
        IQueue<Email> emailQueue = clientInstance.getQueue(EMAIL_QUEUE);
        this.listenerId = emailQueue.addItemListener(this, true);
    }

    @PreDestroy
    public void stop() {
        IQueue<Email> emailQueue = clientInstance.getQueue(EMAIL_QUEUE);
        emailQueue.removeItemListener(this.listenerId);
    }

    @Override
    public void itemAdded(ItemEvent<Email> item) {
        log.info("Email scheduled to be processed " + item.getItem());
    }

    @Override
    public void itemRemoved(ItemEvent<Email> item) {
        log.info("Email is being processed " + item.getItem());
    }

}
