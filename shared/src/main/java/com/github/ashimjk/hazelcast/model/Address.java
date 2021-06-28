package com.github.ashimjk.hazelcast.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address implements Serializable {

    private static final long serialVersionUID = 8519320186233713979L;

    private Long addressId;
    private Long customerId;
    private String city;
    private String country;

}
