package com.github.ashimjk.hazelcast.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {

    @Bean(destroyMethod = "shutdown")
    public HazelcastInstance clientInstance() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setInstanceName("client");
        return HazelcastClient.getOrCreateHazelcastClient(clientConfig);
    }

}
