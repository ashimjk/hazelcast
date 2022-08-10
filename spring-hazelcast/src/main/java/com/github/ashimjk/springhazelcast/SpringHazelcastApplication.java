package com.github.ashimjk.springhazelcast;

import com.github.ashimjk.springhazelcast.model.Person;
import com.github.ashimjk.springhazelcast.repository.PersonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.UUID;

@EnableCaching
@SpringBootApplication
public class SpringHazelcastApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringHazelcastApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(PersonRepository personRepository) {
        return args -> {
            List<Person> persons = List.of(
                    new Person(1L, UUID.randomUUID().toString(), "Ashim Khadka", "ashim@gmail.com"),
                    new Person(2L, UUID.randomUUID().toString(), "Kushal Sherchan", "kushal@gmail.com"),
                    new Person(3L, UUID.randomUUID().toString(), "Shekhar Rai", "shekhar@gmail.com")
            );

            personRepository.saveAll(persons);
        };
    }

}
