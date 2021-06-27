package com.github.ashimjk.hazelcast.service;

import com.github.ashimjk.hazelcast.domain.Customer;
import com.hazelcast.map.EntryProcessor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;

@RequiredArgsConstructor
public class UpdateCustomerDOBEP implements Serializable,
        EntryProcessor<Long, Customer, Boolean> {

    private static final long serialVersionUID = 4273167908165692979L;

    private final LocalDate newDOB;

    @Override
    public Boolean process(Map.Entry<Long, Customer> entry) {
        Customer customer = entry.getValue();
        customer.setDob(newDOB);
        entry.setValue(customer);
        return true;
    }

    @Override
    public EntryProcessor<Long, Customer, Boolean> getBackupProcessor() {
        return this;
    }

}
