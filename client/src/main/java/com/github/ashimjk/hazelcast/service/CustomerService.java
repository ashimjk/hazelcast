package com.github.ashimjk.hazelcast.service;

import com.github.ashimjk.hazelcast.domain.Customer;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.EntryProcessor;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import com.hazelcast.query.impl.predicates.SqlPredicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class CustomerService implements MapNames {

    private IMap<Long, Customer> customerById;
    private final HazelcastInstance clientInstance;

    @PostConstruct
    public void init() {
        customerById = clientInstance.getMap(CUSTOMERS_MAP);
    }

    public Customer getCustomer(Long key) {
        return customerById.get(key);
    }

    public void addCustomer(Customer customer) {
        customerById.put(customer.getId(), customer);
    }

    public void addCustomers(Collection<Customer> customers) {

        Map<Long, Customer> customersLocalMap = new HashMap<>();
        for (Customer customer : customers) {
            customersLocalMap.put(customer.getId(), customer);
        }
        customerById.putAll(customersLocalMap);
    }

    public boolean updateCustomer(Long customerId, Function<Customer, Customer> function) {
        try {
            boolean lockObtained = customerById.tryLock(customerId, 2, TimeUnit.SECONDS);
            try {
                if (!lockObtained) {
                    return false;
                }

                Customer customer = getCustomer(customerId);
                Customer newValue = function.apply(customer);
                customerById.put(customerId, newValue);

            } finally {
                customerById.unlock(customerId);
            }
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        return true;
    }

    public Boolean updateCustomerWithEntryProcessor(Long customerId, EntryProcessor<Long, Customer, Boolean> entryProcessor) {
        return customerById.executeOnKey(customerId, entryProcessor);
    }

    public void deleteCustomer(Customer customer) {
        customerById.delete(customer.getId());
    }

    public Collection<Customer> findCustomer(LocalDate dobStart, LocalDate dobEnd) {

        Predicate<Long, Customer> dobStartPredicate = Predicates.greaterEqual("dob", dobStart);
        Predicate<Long, Customer> dobEndPredicate = Predicates.lessThan("dob", dobEnd);
        Predicate<Long, Customer> andPredicate = Predicates.and(dobStartPredicate, dobEndPredicate);

        return customerById.values(andPredicate);
    }

    public Collection<Customer> findCustomersByEmail(String email) {
        SqlPredicate sqlPredicate = new SqlPredicate("email LIKE '" + email + "'");
        return customerById.values(sqlPredicate);
    }

}
