package com.liceu.sromerom.controllers;

import com.liceu.sromerom.services.NoteServiceImpl;
import com.liceu.sromerom.services.UserService;
import com.liceu.sromerom.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class AppController {

    final
    UserServiceImpl userService;
    NoteServiceImpl noteService;

    public AppController(UserServiceImpl userService, NoteServiceImpl noteService) {
        this.userService = userService;
        this.noteService = noteService;
    }

    @RequestMapping("/")
    public String index() {
        System.out.println("Index site");
        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        System.out.println("Login site");
        return "login";
    }

    @PostMapping("/login")
    public ModelAndView login(@RequestParam("username") String user, @RequestParam("password") String pass, HttpServletRequest req) {
        //Iniciarem sessio sempre i quan els parametres no siguin null i la validacio d'usuari sigui true
        if (user != null && pass != null && userService.validateUser(user, pass)) {
            HttpSession session = req.getSession();
            //model.addAttribute("username", user);
            req.setAttribute("username", user);
            session.setAttribute("userid", userService.getUserId(user));
            return new ModelAndView("redirect:/home");
        }

        req.setAttribute("noError", false);
        return new ModelAndView("login");
        //model.addAttribute("noError", false);
        //return new ModelAndView("login", (Map<String, ?>) model);
    }


    @RequestMapping("/register")
    public String register() {
        return "register";
    }

    @RequestMapping("/home")
    public String home() {
        return "home";
    }

    @RequestMapping("/detail")
    public String detail() {
        return "detail";
    }

    @RequestMapping("/restrictedArea")
    public String error() {
        return "error";
    }
}
