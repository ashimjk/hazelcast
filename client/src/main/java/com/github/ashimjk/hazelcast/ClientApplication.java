package com.github.ashimjk.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

    @Bean(destroyMethod = "shutdown")
    public HazelcastInstance clientInstance() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setInstanceName("client");
        return HazelcastClient.getOrCreateHazelcastClient(clientConfig);
    }

}
