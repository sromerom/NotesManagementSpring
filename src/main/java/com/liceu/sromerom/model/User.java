package com.liceu.sromerom.model;

public class User {
    private long userid;
    private String email;
    private String username;
    private String password;

    public User(long userid, String email, String username, String password) {
        this.setUserid(userid);
        this.setEmail(email);
        this.setUsername(username);
        this.setPassword(password);
    }


    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
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

    @Override
    public String toString() {
        return "User{" +
                "iduser=" + userid +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
