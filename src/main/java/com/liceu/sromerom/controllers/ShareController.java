package com.liceu.sromerom.controllers;


import com.liceu.sromerom.exceptions.CustomGenericException;
import com.liceu.sromerom.services.NoteService;
import com.liceu.sromerom.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpSession;
import java.util.Arrays;

@Controller
public class ShareController {
    @Autowired
    UserService userService;

    @Autowired
    NoteService noteService;

    @Autowired
    HttpSession session;


    @GetMapping("/share/{noteid}")
    public String share(@PathVariable("noteid") Long noteid, Model model) {
        //Si hi ha parametre en la url, procedirem a enviar el usuaris que amb els que ha compartir i carregarem el select amb tots els usuaris
        if (noteid != null) {
            Long userid = (Long) session.getAttribute("userid");
            model.addAttribute("noteid", noteid);
            model.addAttribute("userNoteOwner", noteService.getNoteById(noteid).getUser().getUsername());
            model.addAttribute("users", userService.getUnsharedUsers(userid, noteid));
            model.addAttribute("usersShared", noteService.getPermissionFromSharedUsers(noteid));


            if (noteService.isNoteOwner(userid, noteid) || noteService.hasWritePermission(userid, noteid)) {
                model.addAttribute("action", "/share");
            } else {
                throw new CustomGenericException("Note permission", "Sorry. You don't have access to this note!");
            }
        } else {
            throw new CustomGenericException("Note not found", "Sorry. There was a problem trying to get the note");
        }
        return "shares";
    }

    @PostMapping("/share")
    public String postShare(@RequestParam("users[]") String[] toShare, @RequestParam String permissionMode, @RequestParam("noteid") Long noteid, Model model) {
        Long userid = (Long) session.getAttribute("userid");
        boolean noError = false;
        model.addAttribute("action", "/share");

        //Procedirem a compartir la nota, si entre els usuaris no hi estan compartits
        if (toShare != null && toShare.length > 0 && noteid != null && !userService.existsUserShare(noteid, userid, toShare)) {
            if (noteService.isNoteOwner(userid, noteid) || noteService.hasWritePermission(userid, noteid)) {
                noError = noteService.shareNote(userid, noteid, permissionMode, toShare);
            } else {
                throw new CustomGenericException("Note permission", "Sorry. You don't have access to share this note!");
            }
        }

        if (noError) {
            boolean isSessionUser = Arrays.stream(toShare).anyMatch(username -> username.equalsIgnoreCase(userService.getUserById(userid).getUsername()));
            if (isSessionUser) return "redirect:/home";
            return "redirect:/share/" + noteid;
        }

        model.addAttribute("noerror", false);

        return "shares";
    }

    @GetMapping("/deleteShare/{noteid}")
    public String getDeleteShare(@PathVariable("noteid") Long noteid, Model model) {
        if (noteid != null) {
            Long userid = (Long) session.getAttribute("userid");
            model.addAttribute("username", userService.getUserById(userid).getUsername());
            model.addAttribute("noteid", noteid);
            model.addAttribute("users", userService.getSharedUsers(noteid));
            model.addAttribute("usersShared", noteService.getPermissionFromSharedUsers(noteid));
            model.addAttribute("userNoteOwner", noteService.getNoteById(noteid).getUser().getUsername());



            if (noteService.isNoteOwner(userid, noteid) || noteService.hasWritePermission(userid, noteid)) {
                model.addAttribute("action", "/deleteShare");
            } else {
                throw new CustomGenericException("Note permission", "Sorry. You don't have access to this note!");
            }
        } else {
            throw new CustomGenericException("Note not found", "Sorry. There was a problem trying to get the note");
        }

        return "shares";
    }

    @PostMapping("/deleteShare")
    public String postDeleteShare(@RequestParam("users[]") String[] toDeleteShare, @RequestParam("noteid") Long noteid, Model model) {
        Long userid = (Long) session.getAttribute("userid");
        boolean noError;
        model.addAttribute("action", "/deleteShare");

        //Eliminarem sempre i quan existeixi usuaris a eliminar el share i que existeixi un share amb aquells usuaris
        if (noteid != null && toDeleteShare != null && toDeleteShare.length > 0 && userService.existsUserShare(noteid, userid, toDeleteShare)) {
            if (noteService.isNoteOwner(userid, noteid) || noteService.isSharedNote(userid, noteid) || noteService.hasWritePermission(userid, noteid)) {
                noError = noteService.deleteShareNote(userid, noteid, toDeleteShare);
            } else {
                throw new CustomGenericException("Note permission", "Sorry. You don't have access to this note!");
            }

            if (noError) {
                boolean isSessionUser = Arrays.stream(toDeleteShare).anyMatch(username -> username.equalsIgnoreCase(userService.getUserById(userid).getUsername()));
                if (isSessionUser) return "redirect:/home";
                return "redirect:/deleteShare/" + noteid;
            }
        } else {
            throw new CustomGenericException("Note not found", "Sorry. There was a problem trying to get the note");
        }

        model.addAttribute("noerror", false);
        return "shares";
    }

    @PostMapping("/deleteAllShare")
    public String deleteAllShare(@RequestParam Long noteid, Model model) {
        Long userid = (Long) session.getAttribute("userid");
        boolean noError = false;
        if (noteid != null && userid != null) {
            if (!noteService.isSharedNote(userid, noteid) && !noteService.isNoteOwner(userid, noteid) && !noteService.hasWritePermission(userid, noteid)) {
                throw new CustomGenericException("Note permission", "Sorry. You don't have access to this note!");
            }

            noError = noteService.deleteAllShareNote(userid, noteid);

            if (noError) {

                return "redirect:/home";
            }
        }

        model.addAttribute("noerror", false);
        model.addAttribute("action", "/deleteShare");
        return "shares";
    }

    @PostMapping("/updatePermission")
    public String updatePermission(@RequestParam Long noteid, @RequestParam Long shareduserid, @RequestParam String permissionMode, Model model) {
        Long userid = (Long) session.getAttribute("userid");
        boolean noError = true;
        if (noteid != null && userid != null) {
            if (!noteService.isNoteOwner(userid, noteid) && !noteService.hasWritePermission(userid, noteid)) {
                throw new CustomGenericException("Note permission", "Sorry. You don't have access to this note!");
            }
            noError = noteService.updatePermissionMode(userid, shareduserid, noteid, permissionMode);
        }

        if (noError) {
            if (shareduserid.equals(userid)) return "redirect:/home";
            return "redirect:/share/" + noteid;
        }

        model.addAttribute("noerror", false);
        model.addAttribute("action", "/updatePermission");
        return "shares";
    }
}
