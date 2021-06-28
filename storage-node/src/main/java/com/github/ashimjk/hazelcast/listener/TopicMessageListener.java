package com.github.ashimjk.hazelcast.listener;

import com.github.ashimjk.hazelcast.shared.StoreNames;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.topic.ITopic;
import com.hazelcast.topic.Message;
import com.hazelcast.topic.MessageListener;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TopicMessageListener implements MessageListener<String>, StoreNames {

    private UUID listenerId;
    private final HazelcastInstance hazelcastInstance;

    @PostConstruct
    public void init() {
        ITopic<String> topic = hazelcastInstance.getTopic(NEW_CUSTOMER_TOPIC);
        listenerId = topic.addMessageListener(this);
    }

    @PreDestroy
    public void stop() {
        ITopic<String> topic = hazelcastInstance.getTopic(NEW_CUSTOMER_TOPIC);
        topic.removeMessageListener(listenerId);
    }

    public static void registerItSelf(HazelcastInstance hazelcastInstance) {
        TopicMessageListener entryListener = new TopicMessageListener(hazelcastInstance);
        entryListener.init();
    }

    @Override
    public void onMessage(Message<String> message) {
        log.info("Message received : [{}]", message);
    }

}
