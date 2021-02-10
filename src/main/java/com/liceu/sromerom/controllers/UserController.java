package com.liceu.sromerom.controllers;

import com.liceu.sromerom.entities.User;
import com.liceu.sromerom.exceptions.CustomGenericException;
import com.liceu.sromerom.services.*;
import com.liceu.sromerom.utils.TypeUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URL;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    NoteService noteService;

    @Autowired
    GoogleService googleService;

    @Autowired
    TwitterService twitterService;

    @Autowired
    FacebookService facebookService;

    @Autowired
    HttpSession session;

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @PostMapping("login")
    public String postLogin(@RequestParam("username") String username, @RequestParam("password") String pass, Model model) {
        User user = userService.getUserByUsername(username);

        if (user != null && pass != null) {
            //Iniciarem sessio sempre i quan els parametres no siguin null i la validacio d'usuari sigui true
            if (user.getTypeUser().equals(TypeUser.NATIVE) && userService.validateUser(username, pass)) {
                session.setAttribute("userid", userService.getUserByUsername(username).getUserid());
                return "redirect:/home";
            }
        }

        model.addAttribute("noerror", false);
        return "login";
    }


    @PostMapping("/unlogin")
    public String postUnlogin() {
        session.invalidate();
        return "redirect:/login";
    }


    @GetMapping("/register")
    public String getRegister() {
        return "register";
    }

    @PostMapping("/register")
    public String postRegister(@RequestParam String email, @RequestParam String username,
                               @RequestParam String password, @RequestParam String repeatPassword, Model model) {

        boolean noError = false;
        //Revisam que els parametres que ens passen compleixen amb lo basic
        if (email != null && username != null && password != null && repeatPassword != null) {
            boolean canRegister = userService.checkRegister(email, username, password, repeatPassword);
            //Si les dades poden ser registrades, procedirem a crear un usuari
            if (canRegister) {
                noError = userService.createUser(email, username, password, TypeUser.NATIVE);
            } else {
                noError = false;
            }
        }

        model.addAttribute("noerror", noError);
        return "register";

    }


    @GetMapping("/editProfile")
    public String getEditProfile(Model model) {
        Long userid = (Long) session.getAttribute("userid");
        if (userid != null && userService.getUserById(userid) != null) {
            model.addAttribute("user", userService.getUserById(userid));
        } else {
            throw new CustomGenericException("User permission", "Sorry. You don't have access to this account!");
        }

        return "userProfile";
    }

    @PostMapping("/editProfile")
    public String postEditProfile(@RequestParam(required = false) String newEmail,
                                  @RequestParam(required = false) String newUser,
                                  @RequestParam(required = false) String currentPassword,
                                  @RequestParam(required = false) String newPass,
                                  @RequestParam(required = false) String newPassConfirm,
                                  Model model) {

        Long userid = (Long) session.getAttribute("userid");

        boolean noError = false;

        if (userService.getUserById(userid).getTypeUser().equals(TypeUser.NATIVE)) {

        }


        //Si tenim null el email i username i la resta no, voldra dir que nomes esta modificant la contrasenya
        if (userService.getUserById(userid).getTypeUser().equals(TypeUser.NATIVE) && newEmail == null && newUser == null && currentPassword != null && newPass != null && newPassConfirm != null) {
            boolean validInfo = userService.checkPasswordData(userid, currentPassword, newPass, newPassConfirm);
            if (validInfo) {
                noError = userService.editPassword(userid, newPass);
            }
        }

        //Si tenim null el current password, newPass i newPassConfirm i la resta no, voldra dir que nomes esta modificant el usuari i el correu
        if (currentPassword == null && newPass == null && newPassConfirm == null && newEmail != null && newUser != null) {
            boolean validInfo = userService.checkEditData(userid, newEmail, newUser);
            if (validInfo) {
                noError = userService.editDataInfo(userid, newEmail, newUser);
            }
        }

        if (noError) {
            return "redirect:/home";
        }

        model.addAttribute("noerror", false);
        model.addAttribute("action", "edit");
        model.addAttribute("user", userService.getUserById(userid));
        return "userProfile";
    }


    @PostMapping("/deleteUser")
    public String deleteUser(HttpServletRequest request, Model model) {
        Long userid = (Long) session.getAttribute("userid");

        boolean noError = false;
        if (userid != null) {
            noError = userService.deleteUser(userid);
        }

        if (noError) {
            request.setAttribute(
                    View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
            return "redirect:/unlogin";
        }


        model.addAttribute("noerror", false);
        model.addAttribute("action", "deleteUser");
        return "userProfile";
    }


    @GetMapping("/loginGoogle")
    public String loginGoogle() throws Exception {
        URL url = googleService.getGoogleRedirectURL();
        return "redirect:" + url;
    }

    @GetMapping("/auth/google/oauth2callback/")
    public String oauthCallback(@RequestParam String code, HttpSession session) throws Exception {
        String accessToken = googleService.getAccessToken(code);
        Map<String, String> userDetails = googleService.getUserDetails(accessToken);
        String emailGoogleAccount = userDetails.get("email");

        if (emailGoogleAccount != null) {
            User userIsRegistred = userService.getUserByEmail(emailGoogleAccount);
            boolean noError = false;

            if (userIsRegistred != null) {
                noError = true;
            }

            if (userIsRegistred == null) {
                String usernameGenerated = userService.createNewUsernameFromEmail(userDetails.get("email"));
                noError = userService.createUser(emailGoogleAccount, usernameGenerated, null, TypeUser.GOOGLE);
            }

            if (noError) {
                userIsRegistred = userService.getUserByEmail(emailGoogleAccount);
                session.setAttribute("userid", userIsRegistred.getUserid());
                return "redirect:/home";
            }
        }
        throw new CustomGenericException("Login Google error", "Sorry. There was a problem trying to login with Google account. Try again later.");
    }


    @GetMapping("/loginTwitter")
    public String loginTwitter() {
        Map<String, String> requestToken = twitterService.getRequestToken();
        URL url = twitterService.getUrlRedirectTwitter(requestToken.get("oauth_token"));
        return "redirect: " + url;
    }

    @GetMapping("/auth/twitter/oauth2callback/")
    public String twitterAuthCallback(@RequestParam String oauth_token, @RequestParam String oauth_verifier) {
        Map<String, String> accessTokenUser = twitterService.getAccessToken(oauth_token, oauth_verifier);
        Map<String, Object> verify_credentials = twitterService.getAccountDetails(accessTokenUser.get("oauth_token"), accessTokenUser.get("oauth_token_secret"));
        String emailTwitterAccount = (String) verify_credentials.get("email");


        if (emailTwitterAccount != null) {
            User userIsRegistred = userService.getUserByEmail(emailTwitterAccount);
            boolean noError = false;

            if (userIsRegistred != null) {
                noError = true;
            }

            if (userIsRegistred == null) {
                String usernameGenerated = userService.createNewUsernameFromEmail(emailTwitterAccount);
                noError = userService.createUser(emailTwitterAccount, usernameGenerated, null, TypeUser.TWITTER);
            }

            if (noError) {
                userIsRegistred = userService.getUserByEmail(emailTwitterAccount);
                session.setAttribute("userid", userIsRegistred.getUserid());
                return "redirect:/home";
            }
        } else {
            throw new CustomGenericException("Email Error", "Sorry. If you want entry with twitter account, you must have an email address assigned in your account.");
        }
        throw new CustomGenericException("Login Twitter error", "Sorry. There was a problem trying to login with Twitter account. Try again later.");
    }

    @GetMapping("/loginFacebook")
    public String loginFacebook() throws Exception {
        URL url = facebookService.getFacebookURL();
        return "redirect:" + url;
    }

    @GetMapping("/auth/facebook/oauth2callback/")
    public String facebookAuthCallback(@RequestParam String code, HttpSession session) throws Exception {
        String accessToken = facebookService.getAccessToken(code);
        Map<String, Object> facebookData = facebookService.getData(accessToken);
        String emailFacebookAccount = (String) facebookData.get("email");
        String usernameFacebookAccount = (String) facebookData.get("name");

        if (emailFacebookAccount != null) {
            User userIsRegistred = userService.getUserByEmail(emailFacebookAccount);
            boolean noError = false;

            if (userIsRegistred != null) {
                noError = true;
            }

            if (userIsRegistred == null) {
                noError = userService.createUser(emailFacebookAccount, usernameFacebookAccount, null, TypeUser.FACEBOOK);
            }

            if (noError) {
                userIsRegistred = userService.getUserByEmail(emailFacebookAccount);
                session.setAttribute("userid", userIsRegistred.getUserid());
                return "redirect:/home";
            }
        }
        throw new CustomGenericException("Login Facebook error", "Sorry. There was a problem trying to login with Facebook account. Try again later.");
    }
}
