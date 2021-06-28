package com.github.ashimjk.hazelcast.datastructures;

import com.github.ashimjk.hazelcast.ClientApplication;
import com.github.ashimjk.hazelcast.config.ClientTestConfiguration;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.lock.FencedLock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {"spring.main.allow-bean-definition-overriding=true"},
        classes = {ClientApplication.class, ClientTestConfiguration.class})
public class LockTest {

    private ExecutorService executorService;

    @Autowired private HazelcastInstance clientInstance;

    @BeforeEach
    public void setUp() {
        executorService = Executors.newFixedThreadPool(2);
    }

    @AfterEach
    public void tearDown() {
        executorService.shutdownNow();
    }

    @Test
    public void testLocks() throws Exception {
        PrintOutputRunnable runnable1 = new PrintOutputRunnable(clientInstance, "Runnable One");
        PrintOutputRunnable runnable2 = new PrintOutputRunnable(clientInstance, "Runnable Two");

        Future<?> runnable1Future = executorService.submit(runnable1);
        Thread.sleep(5000);
        assertTrue(runnable1.ownsLock());

        Future<?> runnable2Future = executorService.submit(runnable2);
        Thread.sleep(5000);
        runnable1Future.cancel(true);

        Thread.sleep(5000);
        assertTrue(runnable2.ownsLock());

        runnable2Future.cancel(true);
    }

    public static class PrintOutputRunnable implements Runnable {

        private final HazelcastInstance clientInstance;
        private final String name;
        private boolean ownsLock = false;

        public PrintOutputRunnable(HazelcastInstance clientInstance, String name) {
            this.clientInstance = clientInstance;
            this.name = name;
        }

        @Override
        public void run() {
            FencedLock printingLock = clientInstance.getCPSubsystem().getLock("PrintingLock");
            printingLock.lock();
            try {
                ownsLock = true;
                while (printingLock.isLockedByCurrentThread()) {
                    System.out.println(name + " has lock");
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                printingLock.unlock();
                ownsLock = false;
            }
        }

        public boolean ownsLock() {
            return ownsLock;
        }

    }

}
