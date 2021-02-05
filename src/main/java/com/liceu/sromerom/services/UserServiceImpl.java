package com.liceu.sromerom.services;

import com.liceu.sromerom.entities.Note;
import com.liceu.sromerom.entities.User;
import com.liceu.sromerom.repos.NoteRepo;
import com.liceu.sromerom.repos.UserRepo;
import com.liceu.sromerom.utils.HashUtil;
import com.liceu.sromerom.utils.TypeUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    NoteRepo noteRepo;

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
    public List<User> getUnsharedUsers(long userid, long noteid) {
        Note note = noteRepo.findById(noteid).get();
        List<User> sharedUsers = userRepo.getUsersFromSharedNote(noteid);
        List<User> usersExceptCurrentUser = userRepo.findAll()
                .stream()
                .filter(u -> u.getUserid() != userid)
                .collect(Collectors.toList());

        if (sharedUsers.size() <= 0 ) return usersExceptCurrentUser;
        List<User> unsharedUsers = new ArrayList<>();

        for (User all : usersExceptCurrentUser) {
            boolean add = false;
            for (User sharedUser: sharedUsers) {
                if (all.getUserid() != sharedUser.getUserid() && all.getUserid() != note.getUser().getUserid()) {
                    add = true;
                } else {
                    add = false;
                    break;
                }
            }
            if (add){
                unsharedUsers.add(all);
            }
        }


        return unsharedUsers;
    }

    @Override
    public User getUserById(long userid) {
        return userRepo.findById(userid).get();
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepo.findUserByUsername(username);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public User getUserByEmailAndTypeUser(String email, TypeUser typeUser) {
        return userRepo.findByEmailAndTypeUser(email, typeUser);
    }

    //@Override
    //public User getUserByEmail(String email) {
        //return userRepo.findUserByEmail(email);
    //}

    @Override
    public String createNewUsernameFromEmail(String email) {
        //sromerom@esliceu.net
        final int MIN = 999;
        final int MAX = 10000;
        String newUsername = email.split("@")[0];
        User validateUsername = userRepo.findUserByUsername(newUsername);
        while (validateUsername != null) {
            int randomNumber = (int)(Math.random() * (MAX - MIN + 1) + MIN);
            newUsername += randomNumber;
            validateUsername = userRepo.findUserByUsername(newUsername);
        }
        return newUsername;
    }

    @Transactional
    @Override
    public boolean createUser(String email, String username, String password, TypeUser typeUser) {
        User validateUsername = userRepo.findUserByUsername(username);
        if (validateUsername == null) {
            System.out.println("Entras aqui???????????????????");
            try {
                User newUser = new User();
                String generatedSecuredPassword;
                if (!typeUser.equals(TypeUser.NATIVE)) {
                    generatedSecuredPassword = null;
                } else {
                    generatedSecuredPassword = HashUtil.generatePasswordHash(password);
                }
                newUser.setEmail(email);
                newUser.setUsername(username);
                newUser.setPassword(generatedSecuredPassword);
                newUser.setTypeUser(typeUser);
                User insertedUser = userRepo.save(newUser);
                System.out.println("Ha salido bien??????????????????????: " + insertedUser);
                if (insertedUser != null) return true;
                return false;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean existsUserShare(long noteid, long userid, String[] sharedUsers) {
        System.out.println("UserWhoDeleteShare: " + userid);
        System.out.println("noteid: " + noteid);
        System.out.println("Users to delete: " + Arrays.toString(sharedUsers));

        //User user = userRepo.findById(userid).get();
        //boolean isOwner = noteRepo.existsNoteByNoteidAndUser_Userid(noteid, userid);
        //Note noteOwner = noteRepo.findNoteByNoteidAndUser_Userid(noteid, userid);
        List<User> usersShared = userRepo.getUsersFromSharedNote(noteid);

        //Si no es owner, voldra dir que l'usuari amb qui s'ha compartit la nota, no vol seguir amb aquell share.
        /*
        if (noteOwner == null) {
            usersShared.add(user);
        } else { //L'usuari qui l'ha compartit, vol descompartir-la ara.
            usersShared = userRepo.getUsersFromSharedNote(noteid);
        }
         */
        int aux = 0;
        for (User u : usersShared) {
            for (String sharedUser : sharedUsers) {
                System.out.println(u.getUsername() + "=?" + sharedUser);
                if (u.getUsername().equals(sharedUser)) {
                    aux++;
                    break;
                }
            }
        }

        if (aux == sharedUsers.length) return true;
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
            User validateEmail = userRepo.findByEmail(email);
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
            User validateEmail = userRepo.findByEmail(email);
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

        if (user.getTypeUser().equals(TypeUser.NATIVE)) {
            User updateUser = userRepo.save(user);
            if (updateUser != null) return true;
        }
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

    @Transactional
    @Override
    public boolean deleteUser(long userid) {
        User userToDelete = userRepo.findById(userid).get();
        System.out.println("Usuario a eliminar: " + userToDelete);
        if (userToDelete != null) {
            userRepo.delete(userToDelete);
            if (userRepo.existsById(userToDelete.getUserid())) return false;
            return true;
        }

        return false;
    }
}
