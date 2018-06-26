package com.tantalum.test.message.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
public class Message {

    @Id
    private String uuid;

    @Column
    private String message;

    @Column
    private Timestamp creationDate;
}

