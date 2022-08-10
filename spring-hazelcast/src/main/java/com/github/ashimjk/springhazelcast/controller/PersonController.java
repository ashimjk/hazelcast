package com.github.ashimjk.springhazelcast.controller;

import com.github.ashimjk.springhazelcast.model.Person;
import com.github.ashimjk.springhazelcast.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping("/{id}")
    public Person getPersonById(@PathVariable long id) {
        return personService.getByPersonId(id);
    }

    @GetMapping("/reference/{reference}")
    public Person getPersonById(@PathVariable String reference) {
        return personService.getByPersonReference(reference);
    }

}
