package com.liceu.sromerom.controllers;

import com.liceu.sromerom.entities.User;
import com.liceu.sromerom.services.GoogleService;
import com.liceu.sromerom.services.NoteService;
import com.liceu.sromerom.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
            if (!user.isGoogleUser() && userService.validateUser(username, pass)) {
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
                noError = userService.createUser(email, username, password, false);
            } else {
                noError = false;
            }
        }

        model.addAttribute("noerror", noError);
        return "register";

    }


    @GetMapping("/editProfile")
    public String editProfile() {
        return "userProfile";
    }

    @GetMapping("/loginGoogle")
    public String loginGoogle() throws Exception {
        URL url = googleService.getGoogleRedirectURL();
        return "redirect:" + url;
    }


    @GetMapping("/auth/oauth2callback/")
    public String oauthCallback(@RequestParam String code, HttpSession session) throws Exception {
        String accessToken = googleService.getAccessToken(code);
        Map<String, String> userDetails = googleService.getUserDetails(accessToken);
        String emailGoogleAccount = userDetails.get("email");
        User user = userService.getUserByEmail(emailGoogleAccount);
        boolean noError = false;

        if (user != null && user.isGoogleUser()) {
            System.out.println("Ya esta resgistrado! Nos lo llevamos al home");
            //Ya esta registrado, lo llevamos al home
            noError = true;
        }
        //Quitar en un futuro
        if (user != null && !user.isGoogleUser()) {
            System.out.println("Ya hay una cuenta registrada con dicho email...");
            //Ir a pantalla dedicada
        }

        if (user == null) {
            String usernameGenerated = userService.createNewUsernameFromEmail(userDetails.get("email"));
            System.out.println("Es un usuario nuevo y por eso le creamos este usuario provisional: " + usernameGenerated);
            noError = userService.createUser(emailGoogleAccount, usernameGenerated, null, true);
        }

        if (noError) {
            user = userService.getUserByEmail(emailGoogleAccount);
            session.setAttribute("userid", user.getUserid());
            return "redirect:/home";
        } else {
            return "redirect:/login";
        }
    }
}
