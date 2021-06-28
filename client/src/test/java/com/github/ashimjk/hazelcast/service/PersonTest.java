package com.github.ashimjk.hazelcast.service;

import com.github.ashimjk.hazelcast.ClientApplication;
import com.github.ashimjk.hazelcast.config.ClientTestConfiguration;
import com.github.ashimjk.hazelcast.domain.Person;
import com.github.ashimjk.hazelcast.shared.StoreNames;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = {"spring.main.allow-bean-definition-overriding=true"},
        classes = {ClientApplication.class, ClientTestConfiguration.class})
public class PersonTest {

    @Autowired private HazelcastInstance clientInstance;

    @Test
    public void testAddPerson() {
        Person person = new Person(1L, "Ashim Khadka", LocalDate.now(), "ashim@gmail.com");

        IMap<Long, Person> personMap = clientInstance.getMap(StoreNames.PERSONS_MAP);
        personMap.put(1L, person);

        assertEquals(1, personMap.size());
        assertEquals("Ashim Khadka", personMap.get(1L).getName());
    }

}
