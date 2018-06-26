package com.tantalum.test.message.repositories;

import com.tantalum.test.message.entities.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {

    Message getOne(String uuid);

    Message save(Message message);

    List<Message> findAll();

    void delete(Message message);

    @Query("SELECT m FROM Message m")
    Page<Message> findMessagesTop(Pageable pageable);
}
