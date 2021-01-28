package com.liceu.sromerom.controllers;

import com.liceu.sromerom.services.NoteService;
import com.liceu.sromerom.services.UserService;
import com.liceu.sromerom.utils.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

@Controller
public class NoteController {
    @Autowired
    UserService userService;

    @Autowired
    NoteService noteService;


    @GetMapping("/home")
    public String home(@RequestParam(required = false) Integer currentPage,
                       @RequestParam(required = false) String typeNote,
                       @RequestParam(required = false) String titleFilter,
                       @RequestParam(required = false) String noteStart,
                       @RequestParam(required = false) String noteEnd,
                       HttpServletRequest request, Model model) {

        HttpSession session = request.getSession();
        Long userid = (Long) session.getAttribute("userid");
        String username = userService.getUserById(userid).getUsername();
        model.addAttribute("useridSession", userid);
        model.addAttribute("username", username);

        //Pagination
        final double PAGES_FOR_NOTE = 10.0; //Quantitat de notes que volem per pagina
        int totalPages; //El numero total de pagines que tindra la paginacio, que variara segons el tipus de cerca que facem
        if (currentPage == null) {
            currentPage = 1;
        }

        //Filter per mostrar els tipus de nota (totes les notes juntes, notes creades, notes que t'han compartit i notes que has compartit)
        if (typeNote != null && !typeNote.equals("")) {
            if (typeNote.equals("sharedNotesWithMe")) {
                model.addAttribute("typeNote", typeNote);
                model.addAttribute("notes", noteService.getSharedNoteWithMe(userid, currentPage - 1));
                totalPages = (int) Math.ceil(noteService.getLengthSharedNoteWithMe(userid) / PAGES_FOR_NOTE);
            } else if (typeNote.equals("sharedNotesByYou")) {
                model.addAttribute("typeNote", typeNote);
                model.addAttribute("notes", noteService.getSharedNotes(userid, currentPage - 1));
                totalPages = (int) Math.ceil(noteService.getLengthSharedNotes(userid) / PAGES_FOR_NOTE);
            } else {
                model.addAttribute("typeNote", typeNote);
                model.addAttribute("notes", noteService.getCreatedNotes(userid, currentPage - 1));
                totalPages = (int) Math.ceil(noteService.getCreatedNotesLength(userid) / PAGES_FOR_NOTE);
            }
        } else {
            model.addAttribute("typeNote", "allNotes");
            model.addAttribute("notes", noteService.getNotesFromUser(userid, currentPage - 1));
            totalPages = (int) Math.ceil(noteService.getAllNotesLength(userid) / (PAGES_FOR_NOTE));
        }

        //Aplicam els filtres de cerca ja sigui per date o per search de paraules clau
        if (Filter.checkFilter(titleFilter, noteStart, noteEnd)) {
            model.addAttribute("notes", noteService.filter(userid, typeNote, titleFilter, noteStart, noteEnd, (currentPage - 1)));
            totalPages = (int) Math.ceil(noteService.filter(userid, typeNote, titleFilter, noteStart, noteEnd, (currentPage - 1)).size() / (PAGES_FOR_NOTE));
        }
        //Pasam a la vista tots els parametres corresponents amb la paginacio
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage);

        return "home";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam("id") Long noteid, HttpServletRequest request, Model model) {
        if (noteid != null) {
            HttpSession session = request.getSession();
            Long userid = (Long) session.getAttribute("userid");

            //Pasarem els atributs a la vista sempre i quan la nota sigui compartida amb tu
            if (noteService.isSharedNote(userid, noteid) || noteService.isNoteOwner(userid, noteid)) {
                model.addAttribute("titleNote", noteService);
                model.addAttribute("titleNote", noteService.getNoteById(noteid).getTitle());
                model.addAttribute("bodyNote", noteService.getNoteById(noteid).getBody());
            } else {
                //HACER REDIRECT A RESTRICTED AREA...
                return "redirect:/restrictedArea";
            }
        } else {
            //HACER REDIRECT A HOME..
            return "redirect:/home";
        }
        return "detail";
    }

    @GetMapping("/create")
    public String getCreate(Model model) {
        model.addAttribute("action", "/create");
        return "userForm";
    }

    @PostMapping("/create")
    public String postCreate(@RequestParam String title, @RequestParam("bodyContent") String body, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();

        long userid = (long) session.getAttribute("userid");

        boolean noError = false;
        if (request.getParameter("title") != null && request.getParameter("bodyContent") != null) {
            //Cream la nota...
            noError = noteService.addNote(userid, title, body);
        }


        //Si no hi ha cap error, voldra dir que s'haura creat correctament la nota, i farem un redirect al home
        if (noError) {
            //HACER REDIRECT HACIA HOME...
            return "redirect:/home";
        }

        model.addAttribute("noerror", false);
        model.addAttribute("action", "/create");
        return "userForm";
    }

    @GetMapping("/edit")
    public String getEdit(@RequestParam("id") Long noteid, HttpServletRequest request, Model model) {
        System.out.println("Noteid de la nota que vamos a actualizar: " + noteid);

        if (noteid != null) {
            HttpSession session = request.getSession();
            Long userid = (Long) session.getAttribute("userid");

            model.addAttribute("action", "/edit");

            if (noteService.isNoteOwner(userid, noteid)) {
                System.out.println("Si es note owner!! puede editar!!");
                model.addAttribute("noteid", noteid);
                model.addAttribute("title", noteService.getNoteById(noteid).getTitle());
                model.addAttribute("body", noteService.getNoteById(noteid).getBody());
            } else {
                //HACER REDIRECT RESTRICTED AREA
                return "redirect:/restrictedArea";
            }
        } else {
            //HACER REDIRECT HOME
            return "redirect:/home";

        }
        return "userForm";
    }

    @PostMapping("/edit")
    public String postEdit(@RequestParam String title, @RequestParam("bodyContent") String body, @RequestParam Long noteid, HttpServletRequest request, Model model) {
        boolean noError = false;

        //Si estan tots els parametres necessaris, procedirem a actualitzar la nota
        if (title != null && body != null) {
            //Actualitzam la nota...
            HttpSession session = request.getSession();
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
    public String postDelete(@RequestParam String[] checkboxDelete, HttpServletRequest request, Model model) {
        System.out.println(Arrays.toString(checkboxDelete));
        if (checkboxDelete != null) {
            HttpSession session = request.getSession();
            Long userid = (Long) session.getAttribute("userid");

            //Eliminarem notes sempre i quan tinguem un id d'usuari i tinguem notes per eliminar
            if (userid != null && checkboxDelete.length > 0) {
                System.out.println("Eliminamos las notas: " + Arrays.toString(checkboxDelete));
                noteService.deleteNote(userid, checkboxDelete);
                //REDIRECT HACIA EL HOME...
            } else {
                //REDIRECT HACIA EL RESTRICTED AREA
                return "redirect:/restrictedArea";
            }
        }
        return "redirect:/home";
    }

}
