package com.liceu.sromerom.repos;

import com.liceu.sromerom.entities.User;
import com.liceu.sromerom.utils.TypeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {
    @Query("from user u inner join u.sharedNotes sn where sn.note.noteid = :noteid")
    List<User> getUsersFromSharedNote(@Param("noteid") Long noteid);
    User findUserByUsername(String username);
    User findByEmailAndTypeUser(String email, TypeUser typeUser);
    User findByEmail(String email);
}
