package com.github.ashimjk.springhazelcast.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Person implements Serializable {

    private static final long serialVersionUID = -8590526061918089150L;

    @Id
    private Long id;

    @Column(unique = true)
    private String reference;
    private String name;
    private String email;

}
