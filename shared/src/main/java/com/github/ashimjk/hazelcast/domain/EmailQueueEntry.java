package com.github.ashimjk.hazelcast.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class EmailQueueEntry {

    @Id
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    public Email email;

}
