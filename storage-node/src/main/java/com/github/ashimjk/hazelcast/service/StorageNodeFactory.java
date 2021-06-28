package com.github.ashimjk.hazelcast.service;

import com.github.ashimjk.hazelcast.listener.CustomerEntryListener;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

@Service
public class StorageNodeFactory {

    private final Config storageNodeConfig;
    private final List<HazelcastInstance> instances = Collections.synchronizedList(new ArrayList<>());
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public StorageNodeFactory(@Value("${hazelcast.instance:1}") int instanceCount,
                              Config storageNodeConfig) throws InterruptedException {
        this.storageNodeConfig = storageNodeConfig;
        this.ensureClusterSize(instanceCount);
    }

    public void ensureClusterSize(int size) throws InterruptedException {
        if (instances.size() < size) {
            int diff = size - instances.size();
            CountDownLatch latch = new CountDownLatch(diff);

            for (int x = 0; x < diff; x++) {
                executorService.submit(new CreateHazelcastInstance(latch, storageNodeConfig));
            }

            latch.await(10, TimeUnit.SECONDS);

        } else if (instances.size() > size) {
            for (int x = instances.size() - 1; x >= size; x--) {
                HazelcastInstance instance = instances.remove(x);
                instance.shutdown();
            }
        }
    }

    @PreDestroy
    public void onDestroy() throws InterruptedException {
        this.ensureClusterSize(0);
    }

    @RequiredArgsConstructor
    public final class CreateHazelcastInstance implements Callable<HazelcastInstance> {

        private final CountDownLatch latch;
        private final Config storageNodeConfig;

        @Override
        public HazelcastInstance call() {
            HazelcastInstance instance = Hazelcast.newHazelcastInstance(storageNodeConfig);
            CustomerEntryListener.registerItSelf(instance);
            instances.add(instance);
            if (latch != null) {
                latch.countDown();
            }
            return instance;
        }

    }

}