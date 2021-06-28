package com.github.ashimjk.hazelcast.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOverview implements Serializable {

    private static final long serialVersionUID = -265516475165011427L;

    private String customerName;
    private LocalDate dob;
    private List<Address> addresses;

}
