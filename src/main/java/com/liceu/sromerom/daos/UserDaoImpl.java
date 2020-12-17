package com.liceu.sromerom.daos;

import com.liceu.sromerom.model.User;
import com.liceu.sromerom.utils.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {

    @Override
    public List<User> getAllUsers(long userid) throws Exception {
        List<User> result = new ArrayList<>();
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM user WHERE NOT user_id = ?");
        ps.setLong(1, userid);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            long actualUserid = rs.getLong(1);
            String email = rs.getString(2);
            String username = rs.getString(3);
            String password = rs.getString(4);
            User user = new User(actualUserid, email, username, password);
            result.add(user);
        }
        return result;
    }

    @Override
    public List<User> getUsersFromSharedNote(long noteid) throws Exception {
        List<User> users = new ArrayList<>();
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT user.user_id, email, username, password FROM sharedNote INNER JOIN user ON sharedNote.user_id = user.user_id WHERE note_id = ?");
        ps.setLong(1, noteid);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            long userid = rs.getLong(1);
            String email = rs.getString(2);
            String username = rs.getString(3);
            String password = rs.getString(4);
            users.add(new User(userid, email, username, password));
        }

        ps.close();
        return users;
    }

    @Override
    public User getUserById(long userid) throws Exception {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM user WHERE user_id = ?");
        ps.setLong(1, userid);
        ResultSet rs = ps.executeQuery();
        return new User(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4));

    }

    @Override
    public boolean existsUserWithUsername(String username) throws Exception {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM user WHERE username = ?");
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    @Override
    public boolean existsUserWithEmail(String email) throws Exception {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM user WHERE email = ?");
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    @Override
    public long getUserIdByUsername(String username) throws Exception {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT user_id FROM user WHERE username = ?");
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        return rs.getLong(1);
    }

    @Override
    public void create(User user) throws Exception {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO user (email, username, password) values (?, ?, ?)");
        ps.setString(1, user.getEmail());
        ps.setString(2, user.getUsername());
        ps.setString(3, user.getPassword());
        ps.execute();
        ps.close();

    }

    @Override
    public void updatePasswordById(long userid, String newPassword) throws Exception {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE user SET password = ? WHERE user_id = ?");
        ps.setString(1, newPassword);
        ps.setLong(2, userid);
        ps.execute();
        ps.close();
    }

    @Override
    public void updateDataInfoById(long userid, String email, String username) throws Exception{
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE user SET email = ?, username = ? WHERE user_id = ?");
        ps.setString(1, email);
        ps.setString(2, username);
        ps.setLong(3, userid);
        ps.execute();
        ps.close();
    }
}
