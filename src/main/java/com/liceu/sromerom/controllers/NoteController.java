package com.liceu.sromerom.controllers;

import com.liceu.sromerom.exceptions.CustomGenericException;
import com.liceu.sromerom.services.NoteService;
import com.liceu.sromerom.services.UserService;
import com.liceu.sromerom.services.VersionService;
import com.liceu.sromerom.utils.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class NoteController {
    @Autowired
    UserService userService;

    @Autowired
    NoteService noteService;

    @Autowired
    VersionService versionService;

    @Autowired
    HttpSession session;

    @GetMapping("/home")
    public String home(@RequestParam(required = false) Integer currentPage,
                       @RequestParam(required = false) String typeNote,
                       @RequestParam(required = false) String titleFilter,
                       @RequestParam(required = false) String noteStart,
                       @RequestParam(required = false) String noteEnd,
                       Model model) {

        Long userid = (Long) session.getAttribute("userid");
        String username = userService.getUserById(userid).getUsername();
        model.addAttribute("useridSession", userid);
        model.addAttribute("username", username);

        //Pagination
        final double PAGES_FOR_NOTE = 9.0; //Quantitat de notes que volem per pagina
        int totalPages; //El numero total de pagines que tindra la paginacio, que variara segons el tipus de cerca que facem
        if (currentPage == null) {
            currentPage = 1;
        }

        //Url amb els parametres titleFilter, initDate i endDate + typeNote
        String filterURL = Filter.getURLFilter(typeNote, titleFilter, noteStart, noteEnd);
        model.addAttribute("filterURL", filterURL);

        //Aconseguim les notes segons la cerca que hagui fet l'usuari(pasam el numero -1 indicant que no volem paginar y volem totes les notes)
        model.addAttribute("notes", noteService.filter(userid, typeNote,titleFilter, noteStart, noteEnd, (currentPage - 1)));
        totalPages = (int) Math.ceil(noteService.getAllNotesLength(userid, typeNote,titleFilter, noteStart, noteEnd, -1) / (PAGES_FOR_NOTE));

        model.addAttribute("typeNote", typeNote);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage);

        return "home";
    }

    @GetMapping(value = {"/detail/{noteid}", "/detail/{noteid}/{version}"})
    public String detail(@PathVariable("noteid") Long noteid, @PathVariable(value = "version", required = false) Long version, Model model) {
        if (noteid != null) {
            Long userid = (Long) session.getAttribute("userid");
            model.addAttribute("actualNote", noteService.getNoteById(noteid));
            model.addAttribute("versionUrl", version);
            model.addAttribute("versions", versionService.getVersionsFromNote(noteid));

            //Pasarem els atributs a la vista sempre i quan la nota sigui compartida amb tu o sigui owner
            if (noteService.isSharedNote(userid, noteid) || noteService.isNoteOwner(userid, noteid)) {
                if (version == null) {
                    model.addAttribute("render", "note");
                    model.addAttribute("view", noteService.getNoteById(noteid));
                } else {
                    model.addAttribute("render", "version");
                    model.addAttribute("view", versionService.getVersionById(version));
                }
            } else {
                throw new CustomGenericException("Note permission", "Sorry. You don't have access to this note!");
            }
        } else {
            throw new CustomGenericException("Note not found", "Sorry. There was a problem trying to get the note");
        }
        return "detail";
    }

    @PostMapping("/detail")
    public String postDetail(@RequestParam Long versionid, Model model) {
        Long userid = (Long) session.getAttribute("userid");
        boolean noError = false;
        if (versionid != null) {
            String titleVersion = "Copy of " + versionService.getVersionById(versionid).getTitle();
            String bodyVersion = versionService.getVersionById(versionid).getBody();
            noError = noteService.addNote(userid, titleVersion, bodyVersion);
        }

        if (noError) {
            return "redirect:/detail/" + noteService.getCreatedNotes(userid, -1).get((int) (noteService.getCreatedNotesLength(userid) - 1)).getNoteid();
        }

        model.addAttribute("noerror", false);
        return "detail";
    }

    @GetMapping("/create")
    public String getCreate(Model model) {
        model.addAttribute("action", "/create");
        return "userForm";
    }

    @PostMapping("/create")
    public String postCreate(@RequestParam String title, @RequestParam("bodyContent") String body, Model model) {
        long userid = (long) session.getAttribute("userid");
        boolean noError = false;

        if (title != null && body != null && !body.equals("")) {
            //Cream la nota...
            noError = noteService.addNote(userid, title, body);
        }

        //Si no hi ha cap error, voldra dir que s'haura creat correctament la nota, i farem un redirect al home
        if (noError) {
            return "redirect:/home";
        }

        model.addAttribute("noerror", false);
        model.addAttribute("action", "/create");
        return "userForm";
    }

    @GetMapping("/edit/{noteid}")
    public String getEdit(@PathVariable("noteid") Long noteid, Model model) {
        if (noteid != null) {
            Long userid = (Long) session.getAttribute("userid");
            model.addAttribute("action", "/edit");

            if (noteService.isNoteOwner(userid, noteid) || noteService.hasWritePermission(userid, noteid)) {
                model.addAttribute("noteid", noteid);
                model.addAttribute("note", noteService.getNoteById(noteid));
            } else {
                throw new CustomGenericException("Note permission", "Sorry. You don't have access to this note!");
            }
        } else {
            throw new CustomGenericException("Note not found", "Sorry. There was a problem trying to get the note");
        }
        return "userForm";
    }

    @PostMapping("/edit")
    public String postEdit(@RequestParam String title, @RequestParam("bodyContent") String body, @RequestParam Long noteid, Model model) {
        boolean noError = false;

        //Si estan tots els parametres necessaris, procedirem a actualitzar la nota
        if (title != null && body != null && !title.equals("")) {
            //Actualitzam la nota...
            Long userid = (Long) session.getAttribute("userid");
            noError = noteService.editNote(userid, noteid, title, body);
        }

        if (noError) {
            //Redirect hacia home
            return "redirect:/home";
        }

        model.addAttribute("noerror", false);
        model.addAttribute("action", "/edit");
        return "userForm";
    }

    @PostMapping("/delete")
    public String postDelete(@RequestParam String[] checkboxDelete, Model model) {
        Long userid = (Long) session.getAttribute("userid");
        boolean noError = false;
        if (checkboxDelete != null && checkboxDelete.length > 0) {

            //Eliminarem notes sempre i quan tinguem un id d'usuari i tinguem notes per eliminar
            if (userid != null) {
                noError = noteService.deleteNote(userid, checkboxDelete);

                if (!noError) {
                    throw new CustomGenericException("Note error", "Sorry. Something has wrong deleting the note/s. Try again later");
                }
            }
        }

        if (noError) {
            return "redirect:/home";
        }
        throw new CustomGenericException("Note permission", "Sorry. You don't have access to this note!");
    }
}
