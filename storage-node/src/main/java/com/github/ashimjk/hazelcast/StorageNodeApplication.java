package com.github.ashimjk.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StorageNodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(StorageNodeApplication.class, args);
    }

    @Bean(destroyMethod = "shutdown")
    public HazelcastInstance storageNodeInstance() {
        Config config = new Config();
        config.setInstanceName("storage-node");
        return Hazelcast.getOrCreateHazelcastInstance(config);
    }

}
