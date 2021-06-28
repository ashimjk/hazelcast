package com.github.ashimjk.hazelcast.repository;

import com.github.ashimjk.hazelcast.domain.EmailQueueEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends CrudRepository<EmailQueueEntry, Long> {
}
