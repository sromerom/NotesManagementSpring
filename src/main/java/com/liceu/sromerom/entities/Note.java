package com.liceu.sromerom.entities;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity(name = "note")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noteid;

    private String title;

    @Type(type="text")
    private String body;
    private LocalDateTime creationDate;
    private LocalDateTime lastModification;

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(mappedBy = "sharedNotes")
    Set<User> sharedUsers;


}