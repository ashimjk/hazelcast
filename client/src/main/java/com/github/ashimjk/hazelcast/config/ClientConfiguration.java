package com.github.ashimjk.hazelcast.config;

import com.github.ashimjk.hazelcast.serializations.PersonPortableFactory;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.SerializationConfig;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {

    @Bean(destroyMethod = "shutdown")
    public HazelcastInstance clientInstance() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setInstanceName("client");

        // Person Portable Factory for Serialization
        SerializationConfig serializationConfig = clientConfig.getSerializationConfig();
        serializationConfig.addPortableFactory(PersonPortableFactory.FACTORY_ID, new PersonPortableFactory());

        return HazelcastClient.getOrCreateHazelcastClient(clientConfig);
    }

}
