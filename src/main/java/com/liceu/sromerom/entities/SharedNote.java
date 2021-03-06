package com.liceu.sromerom.entities;

import com.liceu.sromerom.utils.PermissionMode;

import javax.persistence.*;


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


    @Enumerated(EnumType.STRING)
    @Column(name = "permission_mode", nullable = false)
    private PermissionMode permissionMode;


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

    public PermissionMode getPermissionMode() {
        return permissionMode;
    }

    public void setPermissionMode(PermissionMode permissionMode) {
        this.permissionMode = permissionMode;
    }

    @Override
    public String toString() {
        return "SharedNote{" +
                "id=" + id +
                ", user=" + user.getUserid() + user.getUsername() + user.getEmail() +
                ", note=" + note.getNoteid() + note.getTitle() +
                ", permissionMode=" + permissionMode +
                '}';
    }
}
