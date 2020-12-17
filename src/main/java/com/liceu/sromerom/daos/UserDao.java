package com.liceu.sromerom.daos;

import com.liceu.sromerom.model.User;

import java.util.List;

public interface UserDao {
    List<User> getAllUsers(long userid) throws Exception;

    List<User> getUsersFromSharedNote(long noteid) throws Exception;

    User getUserById(long userid) throws Exception;

    boolean existsUserWithUsername(String username) throws Exception;

    boolean existsUserWithEmail(String email) throws Exception;

    long getUserIdByUsername(String username) throws Exception;

    void create(User user) throws Exception;

    void updatePasswordById(long userid, String newPassword) throws Exception;

    void updateDataInfoById(long userid, String email, String username) throws Exception;

}