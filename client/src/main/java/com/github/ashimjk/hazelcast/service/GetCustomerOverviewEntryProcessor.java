package com.github.ashimjk.hazelcast.service;

import com.github.ashimjk.hazelcast.domain.Customer;
import com.github.ashimjk.hazelcast.model.Address;
import com.github.ashimjk.hazelcast.model.AddressKey;
import com.github.ashimjk.hazelcast.model.CustomerOverview;
import com.github.ashimjk.hazelcast.shared.StoreNames;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.map.EntryProcessor;
import com.hazelcast.map.IMap;
import com.hazelcast.query.impl.predicates.SqlPredicate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GetCustomerOverviewEntryProcessor implements Serializable,
        EntryProcessor<Long, Customer, CustomerOverview>,
        HazelcastInstanceAware,
        StoreNames {

    private static final long serialVersionUID = -6274401033477934925L;

    private transient HazelcastInstance hazelcastInstance;

    @Override
    @SuppressWarnings("unchecked")
    public CustomerOverview process(Map.Entry<Long, Customer> entry) {
        Long customerKey = entry.getKey();
        Customer customer = entry.getValue();

        IMap<AddressKey, Address> addressByKey = hazelcastInstance.getMap(ADDRESSES_MAP);
        Set<AddressKey> addressKeys = addressByKey.localKeySet(new SqlPredicate("customerId = " + customerKey));
        List<Address> addresses = new ArrayList<>(addressKeys.size());

        addressKeys.forEach(key -> addresses.add(addressByKey.get(key)));

        return new CustomerOverview(customer.getName(), customer.getDob(), addresses);
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

}
