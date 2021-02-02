package com.liceu.sromerom.controllers;


import com.liceu.sromerom.services.NoteService;
import com.liceu.sromerom.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class ShareController {
    @Autowired
    UserService userService;

    @Autowired
    NoteService noteService;

    @GetMapping("/share")
    public String share(@RequestParam("id") Long noteid, HttpServletRequest request, Model model) {
        //Si hi ha parametre en la url, procedirem a enviar el usuaris que amb els que ha compartir i carregarem el select amb tots els usuaris
        if (noteid != null) {
            HttpSession session = request.getSession();
            Long userid = (Long) session.getAttribute("userid");
            model.addAttribute("username", userService.getUserById(userid).getUsername());
            model.addAttribute("noteid", noteid);
            model.addAttribute("users", userService.getUnsharedUsers(userid, noteid));
            model.addAttribute("usersShared", noteService.getPermissionFromSharedUsers(noteid));


            if (noteService.isNoteOwner(userid, noteid) || noteService.hasWritePermission(userid, noteid)) {
                model.addAttribute("owner", noteService.isNoteOwner(userid, noteid));
                model.addAttribute("action", "/share");
            } else {
                //REDIRECT A RESTRICTED AREA
                return "redirect:/restrictedArea";
            }
        } else {
            //REDIRECT AL HOME
            return "redirect:/home";
        }
        return "shares";
    }

    @PostMapping("/share")
    public String postShare(@RequestParam("users[]") String[] toShare, @RequestParam(required = false) String permissionMode, @RequestParam("noteid") Long noteid, HttpServletRequest request, Model model) {
        Long userid = (Long) request.getSession().getAttribute("userid");
        boolean noError = false;

        if (toShare != null && noteid != null && toShare.length > 0 && !userService.existsUserShare(noteid, userid, toShare)) {
            //noError = ns.shareNote(userid, noteid, sharedUsers);
            System.out.println("-------------------------------------------------------------------------------------------------------");
            System.out.println("Antesssssssssssssssssssssssss: " + permissionMode);
            if (permissionMode == null) {
                System.out.println("Es nullllllllllllllllllllllllllllllll");
                permissionMode = "readmode";
            }

            System.out.println("Despueeeeeeeeeeeeeeeeeeeeeees: " + permissionMode);
            noError = noteService.shareNote(userid, noteid, permissionMode, toShare);
        }

        if (noError) {
            //REDIRECT AL HOME PORQUE HA IDO BIEN
            return "redirect:/home";
        }

        model.addAttribute("noError", false);
        model.addAttribute("action", "/share");

        return "shares";
    }

    @GetMapping("/deleteShare")
    public String getDeleteShare(@RequestParam("id") Long noteid, HttpServletRequest request, Model model) {
        if (noteid != null) {
            Long userid = (Long) request.getSession().getAttribute("userid");
            model.addAttribute("username", userService.getUserById(userid).getUsername());
            model.addAttribute("noteid", noteid);
            model.addAttribute("users", userService.getSharedUsers(noteid));
            model.addAttribute("usersShared", noteService.getPermissionFromSharedUsers(noteid));

            if (noteService.isNoteOwner(userid, noteid) || noteService.hasWritePermission(userid, noteid)) {
                model.addAttribute("action", "/deleteShare");
            } else {
                //REDIRECT A RESTRICTED AREA
                return "redirect:/restrictedArea";
            }
        } else {
            //REDIRECT AL HOME
            return "redirect:/home";
        }

        return "shares";
    }

    @PostMapping("/deleteShare")
    public String postDeleteShare(@RequestParam("users[]") String[] toDeleteShare, @RequestParam("noteid") Long noteid, HttpServletRequest request, Model model) {
        Long userid = (Long) request.getSession().getAttribute("userid");
        boolean noError = false;
        if (noteid != null && toDeleteShare.length > 0 && noteService.isNoteOwner(userid, noteid) || noteService.isSharedNote(userid, noteid) || noteService.hasWritePermission(userid, noteid)) {
            System.out.println("Es note owner y el ha compartido su nota!!!!!!!!!!!");
            //Eliminarem sempre i quan existeixi usuaris a eliminar el share i que existeixi un share amb aquells usuaris
            //Mirar metodo existsUserShare
            if (toDeleteShare != null && userService.existsUserShare(noteid, userid, toDeleteShare)) {
                System.out.println("Existe share con estos usuarios!!!!!!!!!!!!!!");
                noError = noteService.deleteShareNote(userid, noteid, toDeleteShare);
            }

            if (noError) {
                return "redirect:/home";
                //REDIRECT AL HOME...
            }
        } else {
            return "redirect:/restrictedArea";
            //REDIRECT A RESTRICTED AREA
        }

        model.addAttribute("noerror", false);
        model.addAttribute("action", "/deleteShare");
        return "shares";
    }

    @PostMapping("/deleteAllShare")
    public String deleteAllShare(@RequestParam Long noteid, HttpServletRequest request, Model model) {
        Long userid = (Long) request.getSession().getAttribute("userid");
        if (noteid != null && userid != null) {
            if (!noteService.isSharedNote(userid, noteid) && !noteService.isNoteOwner(userid, noteid)) {
                return "redirect:/restrictedArea";
            }

            boolean noerror = noteService.deleteAllShareNote(userid, noteid);
        }
        return "redirect:/home";
    }

    @PostMapping("/updatePermission")
    public String updatePermission(@RequestParam Long noteid, @RequestParam Long shareduserid, @RequestParam String permissionMode, HttpServletRequest request, Model model) {
        Long userid = (Long) request.getSession().getAttribute("userid");
        System.out.println("######################################################################");
        System.out.println(noteid);
        System.out.println(shareduserid);
        System.out.println(permissionMode);
        boolean noError = true;
        if (noteid != null && userid != null) {
            noError = noteService.updatePermissionMode(userid, shareduserid, noteid, permissionMode);
        }

        if (noError) {
            //REDIRECT AL HOME PORQUE HA IDO BIEN
            //return "redirect:/share?id=" + noteid;
            return "redirect:/home";
        }

        model.addAttribute("noError", false);
        return "shares";
    }
}
