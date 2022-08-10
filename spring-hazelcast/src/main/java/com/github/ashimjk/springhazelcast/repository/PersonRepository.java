package com.github.ashimjk.springhazelcast.repository;

import com.github.ashimjk.springhazelcast.model.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {

    Optional<Person> findByReference(String reference);

}
