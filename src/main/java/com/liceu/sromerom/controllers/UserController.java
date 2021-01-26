package com.liceu.sromerom.controllers;

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
    public String postLogin(@RequestParam("username") String user, @RequestParam("password") String pass, HttpServletRequest req) {
        //Iniciarem sessio sempre i quan els parametres no siguin null i la validacio d'usuari sigui true
        if (user != null && pass != null && userService.validateUser(user, pass)) {
            HttpSession session = req.getSession();
            session.setAttribute("userid", userService.getUserByUsername(user).getUserid());
            return "redirect:/home";
        }

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
                noError = userService.createUser(email, username, password);
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

    @GetMapping("/app")
    public String app() throws Exception{
        System.out.println("Entramos a la app!!!!!!!!!!!!!!");
        URL url = googleService.getGoogleRedirectURL();
        System.out.println(url);
        return "redirect:" + url;
    }


    @GetMapping("/auth/oauth2callback/")
    @ResponseBody
    public String oauthCallback(@RequestParam String code, HttpSession session) throws Exception {
        System.out.println("Codigo: " + code);
        String accessToken = googleService.getAccessToken(code);
        Map<String, String> userDetails = googleService.getUserDetails(accessToken);
        session.setAttribute("userDetails", userDetails);
        return "redirect:/success";
    }

    @GetMapping("/success")
    @ResponseBody
    public String success(HttpSession httpSession) {
        Map<String, String> userDetails = (Map<String, String>) httpSession.getAttribute("userDetails");
        if (userDetails == null) {
            return "No autoritzat";
        }

        return userDetails.toString();
    }
}
