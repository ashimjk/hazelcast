package com.github.ashimjk.hazelcast.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Email implements Serializable {

    private static final long serialVersionUID = 831533335315414057L;

    @Id
    private String uuid;
    private String toAddress;
    private String subject;
    private String body;

}
