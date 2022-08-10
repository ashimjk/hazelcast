package com.github.ashimjk.springhazelcast.config;

import com.github.ashimjk.springhazelcast.listeners.PersonEntryListener;
import com.hazelcast.config.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfig {

    @Bean
    public Config config() {
        Config config = new Config();
        config.setInstanceName("spring-hazelcast");

        JoinConfig joinConfig = config.getNetworkConfig().getJoin();
        joinConfig.getMulticastConfig().setEnabled(true);

        MapConfig mapConfig = config.getMapConfig("default");
        mapConfig.setTimeToLiveSeconds(600);
        mapConfig.setMaxIdleSeconds(600);

        EvictionConfig evictionConfig = mapConfig.getEvictionConfig();
        evictionConfig.setEvictionPolicy(EvictionPolicy.LRU);
        evictionConfig.setMaxSizePolicy(MaxSizePolicy.PER_NODE);
        evictionConfig.setSize(1000);

        EntryListenerConfig entryListenerConfig = new EntryListenerConfig();
        entryListenerConfig.setImplementation(new PersonEntryListener());
        entryListenerConfig.setLocal(true);

        mapConfig.addEntryListenerConfig(entryListenerConfig);

        return config;
    }

}
