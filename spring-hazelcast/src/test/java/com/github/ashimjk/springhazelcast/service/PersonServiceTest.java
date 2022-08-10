package com.github.ashimjk.springhazelcast.service;

import com.github.ashimjk.springhazelcast.model.Person;
import com.github.ashimjk.springhazelcast.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class PersonServiceTest {

    @Autowired private PersonService personService;
    @SpyBean private PersonRepository personRepository;

    @Test
    void testGetAllPerson() {
        Person person1 = personService.getByPersonId(1L);
        assertThat(person1.getName()).isEqualTo("Ashim Khadka");

        Person person2 = personService.getByPersonId(2L);
        assertThat(person2.getName()).isEqualTo("Kushal Sherchan");

        Person person3 = personService.getByPersonId(1L);
        assertThat(person3.getName()).isEqualTo("Ashim Khadka");

        verify(personRepository, times(2)).findAll();
    }

}