package com.github.ashimjk.hazelcast.listener;

import com.github.ashimjk.hazelcast.domain.Customer;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.map.MapEvent;

public class ConfigEntryListener implements EntryListener<Long, Customer>, HazelcastInstanceAware {

    private HazelcastInstance hazelcastInstance;

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public void entryAdded(EntryEvent<Long, Customer> event) {
        // Customer customer = event.getValue();
        // if (customer.getEmail() != null) {
        //     String generatedUuid = UUID.randomUUID().toString();
        //     String emailAddress = customer.getEmail();
        //     String subject = "Welcome to our Hazelcast Store";
        //     String body = "Hi " + customer.getName() + ", Thanks for joining our Hazelcast Store.";
        //
        //     Email email = new Email(generatedUuid, emailAddress, subject, body);
        //
        //     IQueue<Email> emailQueue = hazelcastInstance.getQueue(StoreNames.EMAIL_QUEUE);
        //     emailQueue.add(email);
        // }
    }

    @Override
    public void entryEvicted(EntryEvent<Long, Customer> event) {

    }

    @Override
    public void entryExpired(EntryEvent<Long, Customer> event) {

    }

    @Override
    public void entryRemoved(EntryEvent<Long, Customer> event) {

    }

    @Override
    public void entryUpdated(EntryEvent<Long, Customer> event) {

    }

    @Override
    public void mapCleared(MapEvent event) {

    }

    @Override
    public void mapEvicted(MapEvent event) {

    }

}
