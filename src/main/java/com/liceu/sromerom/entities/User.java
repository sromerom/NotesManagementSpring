package com.liceu.sromerom.entities;

import com.liceu.sromerom.utils.TypeUser;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Set;


@Entity(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userid;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;


    @Enumerated(EnumType.STRING)
    @Column(name = "type_user", nullable = false)
    private TypeUser typeUser;

    //RELACIO 1-N amb notes
    @OneToMany(mappedBy = "user", cascade =CascadeType.MERGE, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Note> notes;

    //RELACIO N-N amb notes
    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<SharedNote> sharedNotes;


    //1-n version
    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Version> versions;

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public TypeUser getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(TypeUser typeUser) {
        this.typeUser = typeUser;
    }

    public Set<Note> getNotes() {
        return notes;
    }

    public void setNotes(Set<Note> notes) {
        this.notes = notes;
    }

    public Set<SharedNote> getSharedNotes() {
        return sharedNotes;
    }

    public void setSharedNotes(Set<SharedNote> sharedNotes) {
        this.sharedNotes = sharedNotes;
    }

    public Set<Version> getVersions() {
        return versions;
    }
    public void setVersions(Set<Version> versions) {
        this.versions = versions;
    }

    @Override
    public String toString() {
        return "User{" +
                "userid=" + userid +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", typeUser=" + typeUser +
                '}';
    }
}