package com.liceu.sromerom.services;

import com.liceu.sromerom.entities.Note;
import com.liceu.sromerom.entities.SharedNote;
import com.liceu.sromerom.entities.SharedNoteCK;
import com.liceu.sromerom.entities.User;
import com.liceu.sromerom.repos.NoteRepo;
import com.liceu.sromerom.repos.SharedNoteRepo;
import com.liceu.sromerom.repos.UserRepo;
import com.liceu.sromerom.utils.Filter;
import com.liceu.sromerom.utils.RenderableNote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {

    public final static short LIMIT = 10;
    @Autowired
    NoteRepo noteRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    SharedNoteRepo sharedNoteRepo;

    @Override
    public List<RenderableNote> getNotesFromUser(long userid, int page) {
        Pageable topTen = PageRequest.of(page, LIMIT);
        List<RenderableNote> renderableNotes;
        List<Note> sharedNotes = sharedNoteRepo.findByNote_User_Userid(userid, topTen)
                .stream().map(a -> a.getNote())
                .collect(Collectors.toList());
        List<Note> allNotes = noteRepo.getAllNotesFromUser(userid, topTen);

        try {
            renderableNotes = parseNoteToRenderable(allNotes, sharedNotes);
            return renderableNotes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public List<RenderableNote> getCreatedNotes(long userid, int page) {
        Pageable topTen = PageRequest.of(page, LIMIT);
        List<RenderableNote> renderableNotes = new ArrayList<>();
        List<Note> createdNotes = noteRepo.findByUser_Userid(userid, topTen);

        if (createdNotes != null) {
            createdNotes.forEach(n -> renderableNotes.add(new RenderableNote(n.getNoteid(), n.getUser(), null, n.getTitle(), n.getBody(), n.getCreationDate(), n.getLastModification())));
            return renderableNotes;
        }

        return null;
    }

    @Override
    public long getAllNotesLength(long userid) {
        return noteRepo.countByUser_Userid(userid) + sharedNoteRepo.countByNote_User_Userid(userid);
    }

    @Override
    public long getCreatedNotesLength(long userid) {
        return noteRepo.countByUser_Userid(userid);
    }

    @Override
    public List<RenderableNote> filter(long userid, String type, String search, String initDate, String endDate, int page) {
        System.out.println(type);
        System.out.println("search: " + search);
        System.out.println("initDate " + initDate);
        System.out.println("endDate: " + endDate);
        List<Note> notes;
        try {
            Pageable topTen = PageRequest.of(page, LIMIT);
            if (!Filter.checkTypeFilter(search, initDate, endDate).equals("filterByDate")) {
                System.out.println("Busqueda sin fechas!!!!!!!!!");
                initDate = "1970-01-01 00:00:00";
                endDate = "2021-01-30 00:00:00";
            }

            if (type == null) type = "";

            switch (type) {
                case "sharedNotesWithMe":
                    System.out.println("Filtramos en notas compartidas contigo!!");
                    notes = sharedNoteRepo.filterSharedNotesWithMe(userid, search, initDate, endDate, topTen)
                            .stream().map(a -> a.getNote())
                            .collect(Collectors.toList());
                    break;
                case "sharedNotesByYou":
                    System.out.println("Filtramos en notas compartidas por ti!!");
                    notes = sharedNoteRepo.filterSharedNotes(userid, search, initDate, endDate, topTen)
                            .stream().map(a -> a.getNote())
                            .collect(Collectors.toList());
                    break;
                case "ownerNotes":
                    System.out.println("Filtramos en notas creadas!!");
                    notes = noteRepo.filterCreatedNotes(userid, search, initDate, endDate, topTen);
                    break;
                default:
                    System.out.println("Filtramos en todas las notas!!");
                    notes = noteRepo.filterNotesByAll(userid, search, initDate, endDate, topTen);
                    break;
            }


            //List<Note> createsNoteFilter = noteRepo.filterCreatedNotes(userid, search, initDate, endDate, topTen);
            //List<Note> sharedNoteFilter = sharedNoteRepo.filterSharedNotes(userid, search, initDate, endDate, topTen);
            //List<Note> sharedNoteWithMeFilter = sharedNoteRepo.filterSharedNotesWithMe(userid, search, initDate, endDate, topTen);
            //List<Note> notes = noteRepo.filterNotesByAll(userid, search, initDate, endDate, topTen);
            System.out.println("Notas normales----------------------------------------------------------------------------------------------");
            //System.out.println(notes.toString());
            System.out.println("-----------------------------------------------------");
            System.out.println("Notas creades----------------------------------------------------------------------------------------------");
            //System.out.println(createsNoteFilter.toString());
            //System.out.println(sharedNoteFilter.toString());
            //System.out.println(sharedNoteWithMeFilter.toString());
            System.out.println();
            System.out.println("-----------------------------------------------------");
            /*
            switch (Filter.checkTypeFilter(search, initDate, endDate)) {
                case "filterByTitle":
                    notes = noteRepo.filterNotesBySearch(userid, search, topTen);
                    break;
                case "filterByDate":
                    System.out.println("initDate: " + initDate);
                    System.out.println("endDate: " + endDate);
                    notes = noteRepo.filterNotesByDate(userid, initDate, endDate, topTen);
                    break;
                case "filterAll":
                    notes = noteRepo.filterNotesByAll(userid, search, initDate, endDate, topTen);
                    break;
            }
             */
            List<Note> sharedNotes = sharedNoteRepo.findByNote_User_Userid(userid, topTen)
                    .stream().map(a -> a.getNote())
                    .collect(Collectors.toList());
            List<RenderableNote> renderableNotes = parseNoteToRenderable(notes, sharedNotes);
            return renderableNotes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Note getNoteById(long noteid) {
        return noteRepo.findById(noteid).get();
    }

    @Override
    public boolean isNoteOwner(long userid, long noteid) {

        Note noteOwner = noteRepo.findNoteByNoteidAndUser_Userid(noteid, userid);
        if (noteOwner != null) return true;
        return false;
    }

    @Override
    public boolean isSharedNote(long userid, long noteid) {
        SharedNote existsSharedNote = sharedNoteRepo.findByUser_UseridAndNote_Noteid(userid, noteid);
        if (existsSharedNote != null) return true;
        return false;
    }

    @Transactional
    @Override
    public boolean addNote(long userid, String title, String body) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime myDateObj = LocalDateTime.now();
        String dateString = myDateObj.format(formatter);
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);

        Note newNote = new Note();
        User userOwner = userRepo.findById(userid).get();

        newNote.setTitle(title);
        newNote.setBody(body);
        newNote.setCreationDate(dateTime);
        newNote.setLastModification(dateTime);
        newNote.setUser(userOwner);

        Note insertedNote = noteRepo.save(newNote);
        if (insertedNote != null) return true;
        return false;
    }

    @Transactional
    @Override
    public boolean editNote(long userid, long noteid, String title, String body) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime myDateObj = LocalDateTime.now();
        String dateString = myDateObj.format(formatter);
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);

        if (isNoteOwner(userid, noteid)) {
            Note noteToUpdate = noteRepo.findById(noteid).get();
            noteToUpdate.setTitle(title);
            noteToUpdate.setBody(body);
            noteToUpdate.setLastModification(dateTime);
            Note updatedNote = noteRepo.save(noteToUpdate);
            if (updatedNote != null) return true;
        }
        return false;
    }


    @Transactional
    @Override
    public boolean deleteNote(long userid, String[] noteids) {
        List<Note> notesToDelete = new ArrayList<>();
        for (String noteid : noteids) {
            if (!isNoteOwner(userid, Long.parseLong(noteid))) return false;
            Note noteToDelete = noteRepo.findById(Long.parseLong(noteid)).get();
            notesToDelete.add(noteToDelete);
        }

        for (Note n : notesToDelete) {
            /*
            if (sharedNoteRepo.existsByNote_Noteid(n.getNoteid())) {
                List<String> listUsernames = new ArrayList<>();
                userRepo.getUsersFromSharedNote(n.getNoteid())
                        .stream()
                        .map(ne-> listUsernames.add(ne.getUsername()))
                        .collect(Collectors.toList());
                String[] usernamesToDeleteShare = new String[listUsernames.size()];
                listUsernames.toArray(usernamesToDeleteShare);
                deleteShareNote(userid, n.getNoteid(), usernamesToDeleteShare);
            }
             */
            noteRepo.delete(n);
        }
        return false;
    }


    @Override
    public long getSharedNoteId(long noteid) {
        //return sharedNoteRepo.findBy
        return 0;
    }

    @Override
    public List<RenderableNote> getSharedNoteWithMe(long userid, int page) {
        Pageable topTen = PageRequest.of(page, 10);
        List<RenderableNote> renderableNotes = new ArrayList<>();
        List<Note> sharedNotesWithMe = sharedNoteRepo.findByUser_Userid(userid, topTen)
                .stream().map(a -> a.getNote())
                .collect(Collectors.toList());

        sharedNotesWithMe.forEach(n -> renderableNotes.add(new RenderableNote(n.getNoteid(), n.getUser(), null, n.getTitle(), n.getBody(), n.getCreationDate(), n.getLastModification())));
        return renderableNotes;
    }

    @Override
    public List<RenderableNote> getSharedNotes(long userid, int page) {
        List<RenderableNote> renderableNotes = new ArrayList<>();
        Pageable topTen = PageRequest.of(page, LIMIT);
        List<Note> sharedNotes = sharedNoteRepo.findByNote_User_Userid(userid, topTen)
                .stream().map(a -> a.getNote())
                .collect(Collectors.toList());


        for (Note n : sharedNotes) {
            List<User> sharedUsersFromNote = noteRepo.getUsersFromSharedNote(n.getNoteid());
            renderableNotes.add(new RenderableNote(n.getNoteid(), n.getUser(), sharedUsersFromNote, n.getTitle(), n.getBody(), n.getCreationDate(), n.getLastModification()));
        }

        return renderableNotes;
    }

    @Override
    public long getLengthSharedNoteWithMe(long userid) {
        return sharedNoteRepo.countByUser_Userid(userid);
    }

    @Override
    public long getLengthSharedNotes(long userid) {
        return sharedNoteRepo.countByNote_User_Userid(userid);
    }

    @Transactional
    @Override
    public boolean shareNote(long userWhoShares, long noteid, String[] usernames) {
        Note noteForShare = noteRepo.findById(noteid).get();
        List<User> usersToShare = new ArrayList<>();
        //Si no existeix, ni entrarem al bucle
        if (noteForShare != null) {
            for (String username : usernames) {
                long userid = userRepo.findUserByUsername(username).getUserid();
                //Per guardar l'usuari al que compartirem, abans hem de comprovar si la nota que es vol compartir ja ho esta amb els usuaris introduits. Si no ho esta ho afegim
                //a la llista

                if (sharedNoteRepo.findByUser_UseridAndNote_Noteid(userid, noteid) == null) {
                    User user = userRepo.findById(userid).get();
                    usersToShare.add(user);
                }
            }

            //Mentres hagui usuaris a la llista, crearem el share
            if (usersToShare.size() != 0) {
                for (User u : usersToShare) {
                    User user = userRepo.findById(u.getUserid()).get();
                    SharedNote newSharedNote = new SharedNote();
                    newSharedNote.setNote(noteForShare);
                    newSharedNote.setUser(user);
                    newSharedNote.setId(new SharedNoteCK(user.getUserid(), noteForShare.getNoteid()));

                    // Note insertedNote = noteRepo.save(newNote);
                    SharedNote insertedSharedNote = sharedNoteRepo.save(newSharedNote);
                    if (insertedSharedNote == null) return false;
                }
                return true;
            }
        }
        return false;
    }

    @Transactional
    @Override
    public boolean deleteShareNote(long userWhoDeleteShare, long noteid, String[] usernames) {
        Note noteForDeleteShare = noteRepo.findById(noteid).get();
        List<User> usersToDeleteShare = new ArrayList<>();

        if (noteForDeleteShare != null) {
            for (String username : usernames) {
                long userid = userRepo.findUserByUsername(username).getUserid();
                //Per guardar l'usuari al que descompartirem, abans hem de comprovar si la nota que es vol descompartir ja ho esta compartida previamente amb els usuaris introduits.
                //Si esta compartida, ho afegim a la llista
                if (sharedNoteRepo.findByUser_UseridAndNote_Noteid(userid, noteid) != null) {
                    User user = userRepo.findById(userid).get();
                    usersToDeleteShare.add(user);
                }
            }

            int notesDeleted = 0;
            if (usersToDeleteShare.size() != 0) {
                for (User u : usersToDeleteShare) {
                    User user = userRepo.findById(u.getUserid()).get();
                    SharedNote deleteShareNote = new SharedNote();
                    deleteShareNote.setNote(noteForDeleteShare);
                    deleteShareNote.setUser(user);
                    deleteShareNote.setId(new SharedNoteCK(user.getUserid(), noteForDeleteShare.getNoteid()));

                    // Note insertedNote = noteRepo.save(newNote);
                    sharedNoteRepo.delete(deleteShareNote);
                    notesDeleted++;
                }

                if (usersToDeleteShare.size() == notesDeleted) return true;
            }
        }
        return false;
    }

    @Transactional
    @Override
    public boolean deleteAllShareNote(long userid, long noteid) {
        Pageable topTen = PageRequest.of(0, 100);//Cambiar size!!!
        List<SharedNote> sharedNotes = sharedNoteRepo.findByNote_User_Userid(userid, topTen);
        List<SharedNote> sharedNotesWithMe = sharedNoteRepo.findByUser_Userid(userid, topTen);

        List<Note> parsedSharedNotes = new ArrayList<>();
        List<Note> parsedSharedNotesWithMe = new ArrayList<>();

        for (SharedNote sn : sharedNotes) {
            parsedSharedNotes.add(sn.getNote());
        }

        for (SharedNote sn : sharedNotesWithMe) {
            parsedSharedNotesWithMe.add(sn.getNote());
        }


        boolean canDelete = false;

        //Primer comprovam que la nota que esta compartida es troba a les notes compartides "With Me"
        for (Note n : parsedSharedNotesWithMe) {
            if (n.getNoteid() == noteid) {
                canDelete = true;
            }
        }

        //Si la nota esta compartida amb tu i nomes amb tu, si podem pasar a borrar la nota
        if (canDelete) {
            sharedNoteRepo.deleteSharedNotesByNote_Noteid(noteid);
            return true;
        }

        //Si la nota no ha estat compartida amb mi, farem una segona comprovacio amb les notes que jo he compartides
        for (Note n : parsedSharedNotes) {
            if (n.getUser().getUserid() == userid && n.getNoteid() == noteid) {
                canDelete = true;
            }
        }

        //Si la nota la he compartit jo i nomes jo, podem posar a borrar la nota
        if (canDelete) {
            sharedNoteRepo.deleteSharedNotesByNote_Noteid(noteid);
            return true;
        }
        return false;
    }

    private List<RenderableNote> parseNoteToRenderable(List<Note> allNotes, List<Note> sharedNotes) throws Exception {
        List<RenderableNote> result = new ArrayList<>();
        for (Note note : allNotes) {
            List<User> sharedUsersFromNote = null;
            for (int j = 0; j < sharedNotes.size(); j++) {
                if (sharedNotes.get(j).getNoteid() == note.getNoteid()) {
                    sharedUsersFromNote = userRepo.getUsersFromSharedNote(sharedNotes.get(j).getNoteid());
                    sharedNotes.remove(j);
                    break;
                }
            }
            result.add(new RenderableNote(note.getNoteid(), note.getUser(), sharedUsersFromNote, note.getTitle(), note.getBody(), note.getCreationDate(), note.getLastModification()));
        }
        return result;
    }
}
