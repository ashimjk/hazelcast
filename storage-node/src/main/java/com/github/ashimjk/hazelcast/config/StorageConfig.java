package com.github.ashimjk.hazelcast.config;

import com.github.ashimjk.hazelcast.listener.AlertPartitionLostListener;
import com.github.ashimjk.hazelcast.listener.ConfigEntryListener;
import com.github.ashimjk.hazelcast.service.CustomerMapStore;
import com.github.ashimjk.hazelcast.service.EmailQueueStore;
import com.github.ashimjk.hazelcast.shared.StoreNames;
import com.hazelcast.config.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

    @Bean
    public Config storageNodeConfig(CustomerMapStore customerMapStore,
                                    EmailQueueStore emailQueueStore,
                                    AlertPartitionLostListener partitionLostListener) {
        Config config = new Config();
        config.setInstanceName("storage-node");

        // Listen Partition Lost
        ListenerConfig listenerConfig = new ListenerConfig();
        listenerConfig.setImplementation(partitionLostListener);
        config.addListenerConfig(listenerConfig);

        // Account Customer Multi Map
        MultiMapConfig multiMapConfig = new MultiMapConfig(StoreNames.ACCOUNT_TO_CUSTOMERS);
        multiMapConfig.setValueCollectionType(MultiMapConfig.ValueCollectionType.LIST);
        config.addMultiMapConfig(multiMapConfig);

        // Email Queue Configuration
        QueueConfig emailQueueConfig = new QueueConfig();
        emailQueueConfig.setName(StoreNames.EMAIL_QUEUE);
        QueueStoreConfig emailQueueStoreConfig = new QueueStoreConfig();
        emailQueueStoreConfig.setStoreImplementation(emailQueueStore);

        emailQueueConfig.setQueueStoreConfig(emailQueueStoreConfig);
        config.addQueueConfig(emailQueueConfig);

        // Create a new map configuration for the customers map
        MapConfig customerMapConfig = new MapConfig();

        // Increase the number of synchronous backups of the data
        // customerMapConfig.setBackupCount(3);

        // Increase the number of asynchronous backups of the data
        // customerMapConfig.setAsyncBackupCount(3);

        // Create a map store config for the customer information
        MapStoreConfig customerMapStoreConfig = new MapStoreConfig();
        customerMapStoreConfig.setImplementation(customerMapStore);
        // Enable asynchronous operation
        // customerMapStoreConfig.setWriteDelaySeconds(3);

        // Update the customers map configuration to use the
        // customers map store config we just created
        customerMapConfig.setMapStoreConfig(customerMapStoreConfig);
        customerMapConfig.setName(StoreNames.CUSTOMERS_MAP);

        IndexConfig dobIndexConfig = new IndexConfig(IndexConfig.DEFAULT_TYPE, "dob");
        customerMapConfig.addIndexConfig(dobIndexConfig);

        // Add global entry listener
        EntryListenerConfig entryListenerConfig = new EntryListenerConfig();
        entryListenerConfig.setImplementation(new ConfigEntryListener());
        entryListenerConfig.setLocal(true);

        customerMapConfig.addEntryListenerConfig(entryListenerConfig);

        // Add the customers map config to our storage node config
        config.addMapConfig(customerMapConfig);

        return config;
    }

}
