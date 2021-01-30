package com.liceu.sromerom.entities;

import javax.persistence.*;


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
    @JoinColumn(name = "user_userid")
    private User user;

    @ManyToOne
    @MapsId("noteid")
    @JoinColumn(name = "note_noteid")
    private Note note;

    private String permissionMode;

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

    public String getPermissionMode() {
        return permissionMode;
    }

    public void setPermissionMode(String permissionMode) {
        this.permissionMode = permissionMode;
    }


    @Override
    public String toString() {
        return "SharedNote{" +
                "id=" + id +
                ", user=" + user +
                ", note=" + note +
                ", permissionMode='" + permissionMode + '\'' +
                '}';
    }
}
