package com.github.ashimjk.hazelcast.repository;

import com.github.ashimjk.hazelcast.domain.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
