package com.liceu.sromerom.services;

import com.liceu.sromerom.entities.Note;
import com.liceu.sromerom.entities.SharedNote;
import com.liceu.sromerom.entities.SharedNoteCK;
import com.liceu.sromerom.entities.User;
import com.liceu.sromerom.repos.NoteRepo;
import com.liceu.sromerom.repos.SharedNoteRepo;
import com.liceu.sromerom.repos.UserRepo;
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
            renderableNotes = parseNoteToRenderable(allNotes,sharedNotes);
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
            for (Note n : createdNotes) {
                renderableNotes.add(new RenderableNote(n.getNoteid(), n.getUser(), null, n.getTitle(), n.getBody(), n.getCreationDate(), n.getLastModification()));
            }
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
        return null;
    }

    @Override
    public Note getNoteById(long noteid) {
        return noteRepo.findById(noteid).get();
    }

    @Override
    public boolean isNoteOwner(long userid, long noteid) {

        User user = userRepo.findById(userid).get();
        Note noteOwner = noteRepo.findNoteByNoteidAndUser(noteid, user);
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
            System.out.println("Es owner de esta maravillosa nota? " + isNoteOwner(userid, Long.parseLong(noteid)));
            if (!isNoteOwner(userid, Long.parseLong(noteid))) return false;
            Note noteToDelete = noteRepo.findById(Long.parseLong(noteid)).get();
            notesToDelete.add(noteToDelete);
        }

        for (Note n : notesToDelete) {
            System.out.println("Entro para eliminaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaar");
            System.out.println("Se elimina la nota: ------------------------------------------------------------");
            System.out.println(n);
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
        List<SharedNote> sh = sharedNoteRepo.findByUser_Userid(userid, topTen);
        List<Note> sharedNotesWithMe = new ArrayList<>();
        for (int i = 0; i < sh.size(); i++) {
            sharedNotesWithMe.add(sh.get(i).getNote());
        }

        for (Note n : sharedNotesWithMe) {
            renderableNotes.add(new RenderableNote(n.getNoteid(), n.getUser(), null, n.getTitle(), n.getBody(), n.getCreationDate(), n.getLastModification()));
        }

        return renderableNotes;
    }

    @Override
    public List<RenderableNote> getSharedNotes(long userid, int page) {
        List<RenderableNote> renderableNotes = new ArrayList<>();
        //List<SharedNote> sh = sharedNoteRepo.findByNote_User_Userid(userid);
        Pageable topTen = PageRequest.of(page, LIMIT);
        List<SharedNote> sh = sharedNoteRepo.findByNote_User_Userid(userid, topTen);
        List<Note> sharedNotes = new ArrayList<>();

        for (int i = 0; i < sh.size(); i++) {
            sharedNotes.add(sh.get(i).getNote());
        }

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
        /*
        List<Note> allNotes = new ArrayList<>();
        List<Note> parsedSharedNotes = new ArrayList<>();
        List<Note> parsedSharedNotesWithMe = new ArrayList<>();
        List<RenderableNote> allRenderablesNotes = new ArrayList<>();

        for (SharedNote sn : sharedNotes) {
            parsedSharedNotes.add(sn.getNote());
        }

        for (SharedNote sn : sharedNotesWithMe) {
            parsedSharedNotesWithMe.add(sn.getNote());
        }

        allNotes.addAll(createsNotes);
        allNotes.addAll(parsedSharedNotesWithMe);

        Collections.sort(allNotes, (d1, d2) -> (int) (d2.getNoteid() - d1.getNoteid()));

        for (Note allNote : allNotes) {
            List<User> sharedUsersFromNote = null;
            for (int j = 0; j < sharedNotes.size(); j++) {
                if (sharedNotes.get(j).getNote().getNoteid() == allNote.getNoteid()) {
                    //sharedUsersFromNote = ud.getUsersFromSharedNote(sharedNotes.get(j).getNoteid());
                    sharedUsersFromNote = noteRepo.getUsersFromSharedNote(sharedNotes.get(j).getNote().getNoteid());
                    sharedNotes.remove(j);
                    break;
                }
            }
            allRenderablesNotes.add(new RenderableNote(allNote.getNoteid(), allNote.getUser(), sharedUsersFromNote, allNote.getTitle(), allNote.getBody(), allNote.getCreationDate(), allNote.getLastModification()));
        }
        return allRenderablesNotes;

         */
        return result;
    }
}
