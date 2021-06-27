package com.github.ashimjk.hazelcast.service;

import com.github.ashimjk.hazelcast.domain.Customer;
import com.github.ashimjk.hazelcast.repository.CustomerRepository;
import com.hazelcast.map.MapStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class CustomerMapStore implements MapStore<Long, Customer> {

    private final CustomerRepository customerRepository;

    @Override
    public void store(Long key, Customer value) {
        this.customerRepository.save(value);
    }

    @Override
    public void storeAll(Map<Long, Customer> map) {
        this.customerRepository.saveAll(map.values());
    }

    @Override
    public void delete(Long key) {
        this.customerRepository.deleteById(key);
    }

    @Override
    public void deleteAll(Collection<Long> keys) {
        this.customerRepository.deleteAllById(keys);
    }

    @Override
    public Customer load(Long key) {
        return this.customerRepository.findById(key).orElse(null);
    }

    @Override
    public Map<Long, Customer> loadAll(Collection<Long> keys) {
        Iterable<Customer> customers = this.customerRepository.findAllById(keys);
        return StreamSupport.stream(customers.spliterator(), false)
                            .collect(Collectors.toMap(Customer::getId, Function.identity()));
    }

    @Override
    public Iterable<Long> loadAllKeys() {
        Iterable<Customer> customers = this.customerRepository.findAll();
        return StreamSupport.stream(customers.spliterator(), false)
                            .map(Customer::getId)
                            .collect(Collectors.toList());
    }

}
