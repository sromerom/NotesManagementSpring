package com.liceu.sromerom.model;


import java.time.LocalDateTime;

public class Note {
    private long noteid;
    private User user;
    private String title;
    private String body;
    private LocalDateTime creationDate;
    private LocalDateTime lastModification;

    public Note(long noteid, User user, String title, String body, LocalDateTime creationDate, LocalDateTime lastModification) {
        this.noteid = noteid;
        this.user = user;
        this.title = title;
        this.body = body;

        this.creationDate = creationDate;
        this.lastModification = lastModification;
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //this.creationDate = LocalDateTime.parse(formatter.format(creationDate));
        //this.lastModification = LocalDateTime.parse(formatter.format(lastModification));
    }

    @Override
    public String toString() {
        return "Note{" +
                "noteid=" + noteid +
                ", user=" + user +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", creationDate=" + creationDate +
                ", lastModification=" + lastModification +
                '}';
    }

    public long getNoteid() {
        return noteid;
    }

    public void setNoteid(long noteid) {
        this.noteid = noteid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
}
