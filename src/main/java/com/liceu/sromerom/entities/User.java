package com.liceu.sromerom.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userid;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;
    private String password;

    private boolean isGoogleUser;
    //@OneToMany(fetch = FetchType.EAGER, mappedBy = "user")

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Note> notes;

    @ManyToMany
    @JoinTable(
            name = "sharedNotes",
            joinColumns = @JoinColumn(name = "user_userid"),
            inverseJoinColumns = @JoinColumn(name = "note_noteid"))
    Set<Note> sharedNotes;

}