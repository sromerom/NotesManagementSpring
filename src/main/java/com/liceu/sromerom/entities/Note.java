package com.liceu.sromerom.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;
import org.springframework.lang.Nullable;

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

    //@OneToMany(mappedBy = "note", fetch = FetchType.EAGER)
    //@OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(
            name = "sharedNotes",
            joinColumns = @JoinColumn(name = "note_noteid", nullable = false),
            inverseJoinColumns = @JoinColumn(name="user_userid", nullable = false)
    )
    @ManyToMany(cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<User> sharedUsers;

    public Long getNoteid() {
        return noteid;
    }

    public void setNoteid(Long noteid) {
        this.noteid = noteid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastModification() {
        return lastModification;
    }

    public void setLastModification(LocalDateTime lastModification) {
        this.lastModification = lastModification;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<User> getSharedUsers() {
        return sharedUsers;
    }

    public void setSharedUsers(Set<User> sharedUsers) {
        this.sharedUsers = sharedUsers;
    }

    @Override
    public String toString() {
        return "Note{" +
                "noteid=" + noteid +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", creationDate=" + creationDate +
                ", lastModification=" + lastModification +
                '}';
    }
}
