package com.liceu.sromerom.services;

import com.liceu.sromerom.entities.User;
import com.liceu.sromerom.repos.UserRepo;
import com.liceu.sromerom.utils.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepo userRepo;

    @Override
    public List<User> getAll(long userid) {
        List<User> usersExceptCurrentUser = userRepo.findAll()
                .stream()
                .filter(u -> u.getUserid() != userid)
                .collect(Collectors.toList());
        return usersExceptCurrentUser;
    }

    @Override
    public List<User> getSharedUsers(long noteid) {
        return userRepo.getUsersFromSharedNote(noteid);
    }

    @Override
    public User getUserById(long userid) {
        return userRepo.findById(userid).get();
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepo.findUserByUsername(username);
    }

    @Transactional
    @Override
    public boolean createUser(String email, String username, String password) {
        User validateUsername = userRepo.findUserByUsername(username);
        if (validateUsername == null) {

            try {
                String generatedSecuredPasswordHash = HashUtil.generatePasswordHash(password);
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setUsername(username);
                newUser.setPassword(generatedSecuredPasswordHash);
                User insertedUser = userRepo.save(newUser);
                if (insertedUser != null) return true;
                return false;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean existsUserShare(long noteid, String[] sharedUsers) {
        List<User> usersShared = userRepo.getUsersFromSharedNote(noteid);
        for (User user : usersShared) {
            for (String sharedUser : sharedUsers) {
                if (user.getUsername().equals(sharedUser)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean validateUser(String username, String password) {
        try {
            User userToValidate = userRepo.findUserByUsername(username);
            if (userToValidate != null) {
                String storedPassword = userToValidate.getPassword();
                return HashUtil.validatePassword(password, storedPassword);
            }
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return false;
    }

    @Override
    public boolean checkRegister(String email, String username, String password, String password2) {
        Pattern patternPassword = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
        Matcher matcherPassword = patternPassword.matcher(password);
        boolean passwordMatch = matcherPassword.find();

        Pattern patternUsername = Pattern.compile("^(?=[a-zA-Z0-9._]{3,20}$)(?!.*[_.]{2})[^_.].*[^_.]$");
        Matcher matcherUsername = patternUsername.matcher(username);
        boolean usernameMatch = matcherUsername.find();

        Pattern patternEmail = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        Matcher matcherEmail = patternEmail.matcher(email);
        boolean emailMatch = matcherEmail.find();

        try {
            User validateUsername = userRepo.findUserByUsername(username);
            User validateEmail = userRepo.findUserByEmail(email);
            //Si compleix tots els requisits que s'ha de seguir per fer un registre, retornarem true
            if (password.equals(password2) && validateUsername == null && validateEmail == null && passwordMatch && emailMatch && usernameMatch) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public boolean checkEditData(long userid, String email, String username) {
        try {

            User user = userRepo.findById(userid).get();
            User validateEmail = userRepo.findUserByEmail(email);
            User validateUsername = userRepo.findUserByUsername(username);
            if (email != null && user != null) {
                //Si el email introduit no es igual al seu i ja existeix, retornam false ja que no podem tenir email iguals
                if (!user.getEmail().equals(email) && validateEmail != null) return false;
                Pattern patternEmail = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
                Matcher matcherEmail = patternEmail.matcher(email);
                if (!matcherEmail.find()) return false;

                //Si el usernames introduit no es igual al seu i ja existeix, retornam false ja que no podem tenir usernames iguals
                if (!user.getUsername().equals(username) && validateUsername != null) return false;
                Pattern patternUsername = Pattern.compile("^(?=[a-zA-Z0-9._]{3,20}$)(?!.*[_.]{2})[^_.].*[^_.]$");
                Matcher matcherUsername = patternUsername.matcher(username);
                if (!matcherUsername.find()) return false;
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public boolean checkPasswordData(long userid, String currentPassword, String password, String password2) {
        try {
            User user = userRepo.findById(userid).get();
            boolean validCurrentPassword = validateUser(user.getUsername(), currentPassword);
            if (validCurrentPassword) {
                Pattern patternPassword = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
                Matcher matcherPassword = patternPassword.matcher(password);
                boolean passwordMatch = matcherPassword.find();
                if (passwordMatch && password.equals(password2)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Transactional
    @Override
    public boolean editPassword(long userid, String password) {
        User user = userRepo.findById(userid).get();
        user.setPassword(password);
        User updateUser = userRepo.save(user);
        if (updateUser != null) return true;
        return false;
    }

    @Transactional
    @Override
    public boolean editDataInfo(long userid, String email, String username) {
        User user = userRepo.findById(userid).get();
        user.setEmail(email);
        user.setUsername(username);
        User updateUser = userRepo.save(user);
        if (updateUser != null) return true;
        return false;
    }
}
