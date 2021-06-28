package com.github.ashimjk.hazelcast.listener;

import com.github.ashimjk.hazelcast.domain.Customer;
import com.github.ashimjk.hazelcast.domain.Email;
import com.github.ashimjk.hazelcast.shared.StoreNames;
import com.hazelcast.collection.IQueue;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.map.listener.EntryAddedListener;
import com.hazelcast.map.listener.EntryRemovedListener;
import com.hazelcast.map.listener.EntryUpdatedListener;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.UUID;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerEntryListener implements
        EntryAddedListener<Long, Customer>,
        EntryUpdatedListener<Long, Customer>,
        EntryRemovedListener<Long, Customer>,
        StoreNames {

    private final HazelcastInstance hazelcastInstance;
    private UUID listenerId;

    @PostConstruct
    public void init() {
        IMap<Long, Customer> customerById = hazelcastInstance.getMap(StoreNames.CUSTOMERS_MAP);
        this.listenerId = customerById.addLocalEntryListener(this);
    }

    @PreDestroy
    public void stop() {
        IMap<Long, Customer> customerById = hazelcastInstance.getMap(StoreNames.CUSTOMERS_MAP);
        customerById.removeEntryListener(listenerId);
    }

    public static void registerItSelf(HazelcastInstance hazelcastInstance) {
        CustomerEntryListener entryListener = new CustomerEntryListener(hazelcastInstance);
        entryListener.init();
    }

    @Override
    public void entryAdded(EntryEvent<Long, Customer> event) {
        Customer customer = event.getValue();
        if (customer.getEmail() != null) {
            String generatedUuid = UUID.randomUUID().toString();
            String emailAddress = customer.getEmail();
            String subject = "Welcome to our Hazelcast Store";
            String body = "Hi " + customer.getName() + ", Thanks for joining our Hazelcast Store.";

            Email email = new Email(generatedUuid, emailAddress, subject, body);

            IQueue<Email> emailQueue = hazelcastInstance.getQueue(StoreNames.EMAIL_QUEUE);
            emailQueue.add(email);
        }
    }

    @Override
    public void entryUpdated(EntryEvent<Long, Customer> event) {
        Customer customer = event.getValue();
        if (customer.getEmail() != null) {

            String generatedUuid = UUID.randomUUID().toString();
            String emailAddress = customer.getEmail();
            String subject = "Update of customer details";
            String body = "Hi " + customer.getName() + ", We're just letting you know that we have updated your details.";
            Email email = new Email(generatedUuid, emailAddress, subject, body);

            IQueue<Email> emailQueue = hazelcastInstance.getQueue(StoreNames.EMAIL_QUEUE);
            emailQueue.add(email);
        }

    }

    @Override
    public void entryRemoved(EntryEvent<Long, Customer> event) {
        Customer customer = event.getOldValue();
        if (customer.getEmail() != null) {

            String generatedUuid = UUID.randomUUID().toString();
            String emailAddress = customer.getEmail();
            String subject = "Sorry to see you go";
            String body = "Hi " + customer.getName() + ", We're sorry to see you leave us.";
            Email email = new Email(generatedUuid, emailAddress, subject, body);

            IQueue<Email> emailQueue = hazelcastInstance.getQueue(StoreNames.EMAIL_QUEUE);
            emailQueue.add(email);
        }

    }

}
