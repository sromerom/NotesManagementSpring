package com.liceu.sromerom.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SharedNoteCK implements Serializable {

    @Column(name = "userid")
    private Long userid;

    @Column(name = "noteid")
    private Long noteid;

    public SharedNoteCK() {}
    public SharedNoteCK(Long userid, Long noteid) {
        this.userid = userid;
        this.noteid = noteid;
    }


    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getNoteid() {
        return noteid;
    }

    public void setNoteid(Long noteid) {
        this.noteid = noteid;
    }

    @Override
    public String toString() {
        return "SharedNoteCK{" +
                "userid=" + userid +
                ", noteid=" + noteid +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SharedNoteCK)) return false;
        SharedNoteCK that = (SharedNoteCK) o;
        return userid.equals(that.userid) &&
                noteid.equals(that.noteid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userid, noteid);
    }
}
