package com.github.ashimjk.hazelcast.model;

import com.hazelcast.partition.PartitionAware;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressKey implements Serializable, PartitionAware<Long> {

    private static final long serialVersionUID = 4831946456275350276L;

    private Long addressId;
    private Long customerId;

    @Override
    public Long getPartitionKey() {
        return customerId;
    }

}
