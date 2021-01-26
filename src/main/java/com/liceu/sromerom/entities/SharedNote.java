package com.liceu.sromerom.entities;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;


//@Table(uniqueConstraints={
// @UniqueConstraint(columnNames = {"actor_id, film_id"})
// })

//cascade = CascadeType.ALL, orphan
@Entity(name = "sharedNote")
public class SharedNote {
    @EmbeddedId
    private
    SharedNoteCK id;

    @ManyToOne
    @MapsId("userid")
    private User user;

    @ManyToOne
    @MapsId("noteid")
    private Note note;


    public SharedNoteCK getId() {
        return id;
    }

    public void setId(SharedNoteCK id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "SharedNote{" +
                "id=" + id +
                ", user=" + user +
                ", note=" + note +
                '}';
    }
}
