package com.github.ashimjk.hazelcast.config;

import com.github.ashimjk.hazelcast.serializations.PersonPortableFactory;
import com.github.ashimjk.hazelcast.service.StorageNodeFactory;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.SerializationConfig;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ClientTestConfiguration {

    @Bean(destroyMethod = "shutdown")
    public HazelcastInstance clientInstance(StorageNodeFactory storageNodeFactory) throws InterruptedException {
        storageNodeFactory.ensureClusterSize(1);
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setInstanceName("client");

        // Person Portable Factory for Serialization
        SerializationConfig serializationConfig = clientConfig.getSerializationConfig();
        serializationConfig.addPortableFactory(PersonPortableFactory.FACTORY_ID, new PersonPortableFactory());

        return HazelcastClient.getOrCreateHazelcastClient(clientConfig);
    }

}
