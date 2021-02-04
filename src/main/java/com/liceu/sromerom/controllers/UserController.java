package com.liceu.sromerom.controllers;

import com.liceu.sromerom.entities.User;
import com.liceu.sromerom.services.GoogleService;
import com.liceu.sromerom.services.NoteService;
import com.liceu.sromerom.services.TwitterService;
import com.liceu.sromerom.services.UserService;
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

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @PostMapping("login")
    public String postLogin(@RequestParam("username") String username, @RequestParam("password") String pass, HttpServletRequest req, Model model) {
        //Iniciarem sessio sempre i quan els parametres no siguin null i la validacio d'usuari sigui true
        User user = userService.getUserByUsername(username);

        //Iniciarem sessio sempre i quan els parametres no siguin null i la validacio d'usuari sigui true
        if (user != null && pass != null) {
            if (user.getTypeUser().equals(TypeUser.NATIVE) && userService.validateUser(username, pass)) {
                HttpSession session = req.getSession();
                session.setAttribute("userid", userService.getUserByUsername(username).getUserid());
                return "redirect:/home";
            }
        }

        model.addAttribute("noerror", false);
        return "login";
    }


    @PostMapping("/unlogin")
    public String postUnlogin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();

        //HACER REDIRECT AL LOGIN
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
    public String getEditProfile(HttpServletRequest request, Model model) {
        Long userid = (Long) request.getSession().getAttribute("userid");
        if (userid != null && userService.getUserById(userid) != null) {
            model.addAttribute("typeUser", userService.getUserById(userid).getTypeUser());
            model.addAttribute("username", userService.getUserById(userid).getUsername());
            model.addAttribute("email", userService.getUserById(userid).getEmail());
        } else {
            return "redirect:/restrictedArea";
        }

        return "userProfile";
    }

    @PostMapping("/editProfile")
    public String postEditProfile(@RequestParam(required = false)  String newEmail,
                                  @RequestParam(required = false) String newUser,
                                  @RequestParam(required = false) String currentPassword,
                                  @RequestParam(required = false) String newPass,
                                  @RequestParam(required = false) String newPassConfirm,
                                  HttpServletRequest request,
                                  Model model) {

        Long userid = (Long) request.getSession().getAttribute("userid");

        boolean noError = false;

        //Si tenim null el email i username i la resta no, voldra dir que nomes esta modificant la contrasenya
        if (newEmail == null && newUser == null && currentPassword != null && newPass != null && newPassConfirm != null) {
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
        model.addAttribute("typeUser", userService.getUserById(userid).getTypeUser());
        model.addAttribute("username", userService.getUserById(userid).getUsername());
        model.addAttribute("email", userService.getUserById(userid).getEmail());
        return "userProfile";
    }


    @PostMapping("/deleteUser")
    public String deleteUser(HttpServletRequest request, Model model) {
        Long userid = (Long) request.getSession().getAttribute("userid");

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

    @GetMapping("/loginTwitter")
    public String loginTwitter() throws Exception {
        Map<String, String> requestToken = twitterService.getRequestToken();
        System.out.println("Result: " + requestToken);
        URL url = twitterService.getUrlRedirectTwitter(requestToken.get("oauth_token"));
        return "redirect: "+ url;
    }

    //http://127.0.0.1:8080/auth/twitter/oauth2callback/
    @GetMapping("/auth/twitter/oauth2callback/")
    public String twitterAuthCallback(@RequestParam String oauth_token, @RequestParam String oauth_verifier, HttpServletRequest request) throws Exception {
        Map<String, String> accessTokenUser = twitterService.getAccessToken(oauth_token, oauth_verifier);
        System.out.println("oauth_token: " + accessTokenUser.get("oauth_token"));
        System.out.println("oauth_secret_token: " + accessTokenUser.get("oauth_token_secret"));
        Map<String, Object> verify_credentials = twitterService.getAccountDetails(accessTokenUser.get("oauth_token"), accessTokenUser.get("oauth_token_secret"));
        String emailTwitterAccount = (String) verify_credentials.get("email");


        if (emailTwitterAccount != null) {
            User userIsRegistred = userService.getUserByEmail(emailTwitterAccount);
            boolean noError = false;

            if (userIsRegistred != null) {
                System.out.println("Ya esta resgistrado! Nos lo llevamos al home");
                //Ya esta registrado, lo llevamos al home
                noError = true;
            }

            if (userIsRegistred == null) {
                String usernameGenerated = userService.createNewUsernameFromEmail(emailTwitterAccount);
                System.out.println("Es un usuario nuevo y por eso le creamos este usuario provisional: " + usernameGenerated);
                noError = userService.createUser(emailTwitterAccount, usernameGenerated, null, TypeUser.TWITTER);
            }

            if (noError) {
                userIsRegistred = userService.getUserByEmail(emailTwitterAccount);
                request.getSession().setAttribute("userid", userIsRegistred.getUserid());
                return "redirect:/home";
            }
        } else {
            return "redirect:/emailNotFound";
        }

        return "redirect:/login";
    }


    @GetMapping("/auth/oauth2callback/")
    public String oauthCallback(@RequestParam String code, HttpSession session) throws Exception {
        String accessToken = googleService.getAccessToken(code);
        Map<String, String> userDetails = googleService.getUserDetails(accessToken);
        String emailGoogleAccount = userDetails.get("email");
        User userIsRegistred = userService.getUserByEmail(emailGoogleAccount);
        boolean noError = false;


        if (userIsRegistred != null) {
            System.out.println("Ya esta resgistrado! Nos lo llevamos al home");
            //Ya esta registrado, lo llevamos al home
            noError = true;
        }
        /*
        //Quitar en un futuro
        if (user != null && !user.isTypeUser()) {
            System.out.println("Ya hay una cuenta registrada con dicho email...");
            //Ir a pantalla dedicada
        }
         */

        if (userIsRegistred == null) {
            String usernameGenerated = userService.createNewUsernameFromEmail(userDetails.get("email"));
            System.out.println("Es un usuario nuevo y por eso le creamos este usuario provisional: " + usernameGenerated);
            noError = userService.createUser(emailGoogleAccount, usernameGenerated, null, TypeUser.GOOGLE);
        }

        if (noError) {
            userIsRegistred = userService.getUserByEmail(emailGoogleAccount);
            session.setAttribute("userid", userIsRegistred.getUserid());
            return "redirect:/home";
        } else {
            return "redirect:/login";
        }
    }
}
