package com.liceu.sromerom.services;

import com.liceu.sromerom.daos.UserDao;
import com.liceu.sromerom.daos.UserDaoImpl;
import com.liceu.sromerom.model.User;
import com.liceu.sromerom.utils.HashUtil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserServiceImpl implements UserService {
    @Override
    public List<User> getAll(long userid) {
        UserDao ud = new UserDaoImpl();
        List<User> users;
        try {
            users = ud.getAllUsers(userid);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return users;
    }

    @Override
    public List<User> getSharedUsers(long noteid) {
        UserDao ud = new UserDaoImpl();
        try {
            return ud.getUsersFromSharedNote(noteid);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User getUserById(long userid) {
        UserDao ud = new UserDaoImpl();
        try {
            return ud.getUserById(userid);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean validateUser(String username, String password) {
        UserDao ud = new UserDaoImpl();


        try {
            //Nomes retornara true quan l'usuari existeixi i la validacio de la contrasenya introduida es la correcta
            if (ud.existsUserWithUsername(username)) {
                long userid = ud.getUserIdByUsername(username);
                String storedPassword = ud.getUserById(userid).getPassword();
                return HashUtil.validatePassword(password, storedPassword);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public boolean checkRegister(String email, String username, String password, String password2) {
        UserDao ud = new UserDaoImpl();
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
            //Si compleix tots els requisits que s'ha de seguir per fer un registre, retornarem true
            if (password.equals(password2) && !ud.existsUserWithUsername(username) && !ud.existsUserWithEmail(email) && passwordMatch && emailMatch && usernameMatch) {
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
        UserDao ud = new UserDaoImpl();
        try {
            User user = ud.getUserById(userid);

            if (email != null && user != null) {
                //Si el email introduit no es igual al seu i ja existeix, retornam false ja que no podem tenir email iguals
                if (!user.getEmail().equals(email) && ud.existsUserWithEmail(email)) return false;
                Pattern patternEmail = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
                Matcher matcherEmail = patternEmail.matcher(email);
                if (!matcherEmail.find()) return false;

                //Si el usernames introduit no es igual al seu i ja existeix, retornam false ja que no podem tenir usernames iguals
                if (!user.getUsername().equals(username) && ud.existsUserWithUsername(username)) return false;
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
        UserDao ud = new UserDaoImpl();
        try {
            User user = ud.getUserById(userid);
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

    @Override
    public boolean editPassword(long userid, String password) {
        UserDao ud = new UserDaoImpl();
        try {
            String generatedSecuredPasswordHash = HashUtil.generatePasswordHash(password);
            ud.updatePasswordById(userid, generatedSecuredPasswordHash);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean editDataInfo(long userid, String email, String username) {
        UserDao ud = new UserDaoImpl();
        try {
            ud.updateDataInfoById(userid, email, username);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public long getUserId(String username) {
        UserDao ud = new UserDaoImpl();
        try {
            return ud.getUserIdByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public boolean createUser(String email, String username, String password) {
        try {
            UserDao ud = new UserDaoImpl();

            if (!ud.existsUserWithUsername(username)) {
                String generatedSecuredPasswordHash = HashUtil.generatePasswordHash(password);
                User user = new User(0, email, username, generatedSecuredPasswordHash);
                ud.create(user);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public boolean existsUserShare(long noteid, String[] sharedUsers) {
        UserDao ud = new UserDaoImpl();

        try {
            List<User> usersShared = ud.getUsersFromSharedNote(noteid);
            for (User user : usersShared) {
                for (String sharedUser : sharedUsers) {
                    if (user.getUsername().equals(sharedUser)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }
}
