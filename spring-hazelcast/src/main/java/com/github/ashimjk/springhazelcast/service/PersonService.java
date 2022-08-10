package com.github.ashimjk.springhazelcast.service;

import com.github.ashimjk.springhazelcast.model.Person;
import com.github.ashimjk.springhazelcast.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    @Cacheable(key = "#id", cacheNames = "persons")
    public Person getByPersonId(Long id) {
        System.out.println("PersonService.getByPersonId");
        // simulateSlowService();

        return this.personRepository.findById(id)
                                    .orElseThrow(IllegalArgumentException::new);
    }

    @Cacheable(key = "#reference", cacheNames = "persons")
    public Person getByPersonReference(String reference) {
        System.out.println("PersonService.getByPersonReference");
        // simulateSlowService();

        return this.personRepository.findByReference(reference)
                                    .orElseThrow(IllegalArgumentException::new);
    }

    private void simulateSlowService() {
        try {
            long time = 3000L;
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

}
