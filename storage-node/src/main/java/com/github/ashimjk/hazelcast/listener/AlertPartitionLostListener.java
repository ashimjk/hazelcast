package com.github.ashimjk.hazelcast.listener;

import com.hazelcast.partition.PartitionLostEvent;
import com.hazelcast.partition.PartitionLostListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AlertPartitionLostListener implements PartitionLostListener {

    @Override
    public void partitionLost(PartitionLostEvent event) {
        log.error("Partition was lost: [{}]", event);
    }

}
