package com.github.ashimjk.hazelcast.service;

import com.github.ashimjk.hazelcast.domain.Email;
import com.github.ashimjk.hazelcast.domain.EmailQueueEntry;
import com.github.ashimjk.hazelcast.repository.EmailRepository;
import com.hazelcast.collection.QueueStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class EmailQueueStore implements QueueStore<Email> {

    private final EmailRepository emailRepository;

    @Override
    public void store(Long key, Email value) {
        emailRepository.save(new EmailQueueEntry(key, value));
    }

    @Override
    public void storeAll(Map<Long, Email> map) {
        List<EmailQueueEntry> emails = new ArrayList<>(map.size());

        for (Map.Entry<Long, Email> entry : map.entrySet()) {
            EmailQueueEntry dbEntry = new EmailQueueEntry(entry.getKey(), entry.getValue());
            emails.add(dbEntry);
        }

        emailRepository.saveAll(emails);
    }

    @Override
    public void delete(Long key) {
        emailRepository.deleteById(key);
    }

    @Override
    public void deleteAll(Collection<Long> keys) {
        emailRepository.deleteAllById(keys);
    }

    @Override
    public Email load(Long key) {
        return emailRepository.findById(key).map(EmailQueueEntry::getEmail).orElse(null);
    }

    @Override
    public Map<Long, Email> loadAll(Collection<Long> keys) {
        Iterable<EmailQueueEntry> emails = emailRepository.findAllById(keys);
        Map<Long, Email> map = new HashMap<>(keys.size());
        for (EmailQueueEntry entry : emails) {
            map.put(entry.getId(), entry.getEmail());
        }
        return map;
    }

    @Override
    public Set<Long> loadAllKeys() {
        Iterable<EmailQueueEntry> emails = emailRepository.findAll();
        Set<Long> keys = new HashSet<>();
        for (EmailQueueEntry entry : emails) {
            keys.add(entry.getId());
        }
        return keys;
    }

}
