package com.github.ashimjk.hazelcast.config;

import com.github.ashimjk.hazelcast.service.CustomerMapStore;
import com.github.ashimjk.hazelcast.service.EmailQueueStore;
import com.hazelcast.config.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

    @Bean
    public Config storageNodeConfig(CustomerMapStore customerMapStore, EmailQueueStore emailQueueStore) {
        Config config = new Config();
        config.setInstanceName("storage-node");

        // Account Customer Multi Map
        MultiMapConfig multiMapConfig = new MultiMapConfig("account-to-customers");
        multiMapConfig.setValueCollectionType(MultiMapConfig.ValueCollectionType.LIST);
        config.addMultiMapConfig(multiMapConfig);

        // Email Queue Configuration
        QueueConfig emailQueueConfig = new QueueConfig();
        emailQueueConfig.setName("email-queue");
        QueueStoreConfig emailQueueStoreConfig = new QueueStoreConfig();
        emailQueueStoreConfig.setStoreImplementation(emailQueueStore);

        emailQueueConfig.setQueueStoreConfig(emailQueueStoreConfig);
        config.addQueueConfig(emailQueueConfig);

        // Create a new map configuration for the customers map
        MapConfig customerMapConfig = new MapConfig();

        // Create a map store config for the customer information
        MapStoreConfig customerMapStoreConfig = new MapStoreConfig();
        customerMapStoreConfig.setImplementation(customerMapStore);
        // Enable asynchronous operation
        // customerMapStoreConfig.setWriteDelaySeconds(3);

        // Update the customers map configuration to use the
        // customers map store config we just created
        customerMapConfig.setMapStoreConfig(customerMapStoreConfig);
        customerMapConfig.setName("customers");

        IndexConfig dobIndexConfig = new IndexConfig(IndexConfig.DEFAULT_TYPE, "dob");
        customerMapConfig.addIndexConfig(dobIndexConfig);

        // Add the customers map config to our storage node config
        config.addMapConfig(customerMapConfig);

        return config;
    }

}
