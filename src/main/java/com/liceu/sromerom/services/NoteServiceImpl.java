package com.liceu.sromerom.services;

import com.liceu.sromerom.daos.*;
import com.liceu.sromerom.model.Note;
import com.liceu.sromerom.model.User;
import com.liceu.sromerom.utils.Filter;
import com.liceu.sromerom.utils.RenderableNote;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class NoteServiceImpl implements NoteService {
    private final int LIMIT = 10;

    @Override
    public List<RenderableNote> getNotesFromUser(long userid, int offset) {
        List<Note> notes;
        List<RenderableNote> renderableNotes = new ArrayList<>();
        NoteDao nd = new NoteDaoImpl();
        UserDao ud = new UserDaoImpl();
        try {
            List<Note> sharedNotes = nd.getSharedNotes(userid, LIMIT, offset);
            notes = nd.getAllNotesFromUser(userid, LIMIT, offset);
            //Transformam les notes a RenderableNotes per poder saber si una nota ha estat compartida o no sense modificar el model "note"
            parseNoteToRenderable(notes, renderableNotes, ud, sharedNotes);
            return renderableNotes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<RenderableNote> getCreatedNotes(long userid, int offset) {
        List<RenderableNote> renderableNotes = new ArrayList<>();
        NoteDao nd = new NoteDaoImpl();
        try {
            List<Note> createdNotes = nd.getCreatedNotesFromUser(userid, LIMIT, offset);
            for (Note n : createdNotes) {
                renderableNotes.add(new RenderableNote(n.getNoteid(), n.getUser(), null, n.getTitle(), n.getBody(), n.getCreationDate(), n.getLastModification()));
            }
            return renderableNotes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public long getAllNotesLength(long id) {
        NoteDao nd = new NoteDaoImpl();
        try {
            return nd.getNotesLengthFromUser(id) + nd.getSharedNotesWithMeLength(id);
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public long getCreatedNotesLength(long id) {
        NoteDao nd = new NoteDaoImpl();
        try {
            return nd.getNotesLengthFromUser(id);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<RenderableNote> filter(long userid, String type, String search, String initDate, String endDate, int offset) {
        NoteDao nd = new NoteDaoImpl();
        UserDao ud = new UserDaoImpl();
        List<Note> notes = new ArrayList<>();
        List<RenderableNote> renderableNotes = new ArrayList<>();

        try {

            //Si el type de note a filtrar es null, voldra dir que haurem de filtrar a totes les notes
            if (type == null || type.equals("")) {
                switch (Filter.checkTypeFilter(search, initDate, endDate)) {
                    case "filterByTitle":
                        notes = nd.filterAllTypesOfNoteBySearch(userid, search, LIMIT, offset);
                        break;
                    case "filterByDate":
                        notes = nd.filterAllTypesOfNoteByDate(userid, initDate, endDate, LIMIT, offset);
                        break;
                    case "filterAll":
                        notes = nd.filterAllTypesOfNoteByAll(userid, search, initDate, endDate, LIMIT, offset);
                        break;
                }


            } else { //Si no, haurem de filtrar per un tipus de nota en concret (notes creades, notes compartides amb mi o notes que he compartit)
                if (Filter.checkTypeFilter(search, initDate, endDate).equals("filterByTitle")) {
                    if (type.equals("sharedNotesWithMe")) {
                        notes = nd.filterSharedNotesWithMeBySearch(userid, search, LIMIT, offset);
                    } else if (type.equals("sharedNotesByYou")) {
                        notes = nd.filterSharedNotesBySearch(userid, search, LIMIT, offset);
                    } else {
                        notes = nd.filterCreatedNotesBySearch(userid, search, LIMIT, offset);
                    }
                }
                if (Filter.checkTypeFilter(search, initDate, endDate).equals("filterByDate")) {
                    if (type.equals("sharedNotesWithMe")) {
                        notes = nd.filterSharedNotesWithMeByDate(userid, initDate, endDate, LIMIT, offset);
                    } else if (type.equals("sharedNotesByYou")) {
                        notes = nd.filterSharedNotesByDate(userid, initDate, endDate, LIMIT, offset);
                    } else {
                        notes = nd.filterCreatedNotesByDate(userid, initDate, endDate, LIMIT, offset);
                    }
                }
                if (Filter.checkTypeFilter(search, initDate, endDate).equals("filterAll")) {
                    if (type.equals("sharedNotesWithMe")) {
                        notes = nd.filterAllSharedNotesWithMe(userid, search, initDate, endDate, LIMIT, offset);
                    } else if (type.equals("sharedNotesByYou")) {
                        notes = nd.filterAllSharedNotes(userid, search, initDate, endDate, LIMIT, offset);
                    } else {
                        notes = nd.filterAllCreatedNotes(userid, search, initDate, endDate, LIMIT, offset);
                    }
                }
            }

            List<Note> sharedNotes = nd.getSharedNotes(userid, LIMIT, offset);
            //Transformam les notes a RenderableNotes per poder saber si una nota ha estat compartida o no sense modificar el model "note"
            parseNoteToRenderable(notes, renderableNotes, ud, sharedNotes);

            return renderableNotes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Note getNoteById(long noteid) {
        NoteDao nd = new NoteDaoImpl();
        try {
            return nd.getNoteById(noteid);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean isNoteOwner(long userid, long noteid) {
        NoteDao nd = new NoteDaoImpl();
        try {
            return nd.isOwnerNote(userid, noteid);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isSharedNote(long userid, long noteid) {
        NoteDao nd = new NoteDaoImpl();
        try {
            return nd.sharedNoteExists(userid, noteid);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addNote(long userid, String title, String body) {
        NoteDao nd = new NoteDaoImpl();
        UserDao ud = new UserDaoImpl();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime myDateObj = LocalDateTime.now();
        String dateString = myDateObj.format(formatter);
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);

        try {
            nd.create(new Note(0, ud.getUserById(userid), title, body, dateTime, dateTime));
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean editNote(long userid, long noteid, String title, String body) {
        NoteDao nd = new NoteDaoImpl();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime myDateObj = LocalDateTime.now();
        String dateString = myDateObj.format(formatter);
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);




        try {
            Note noteToUpdate = nd.getNoteById(noteid);
            if (nd.isOwnerNote(userid, noteid)) {


                nd.update(new Note(noteid, noteToUpdate.getUser(), title, body, noteToUpdate.getCreationDate(), dateTime));
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public boolean deleteNote(long userid, String[] noteids) {
        NoteDao nd = new NoteDaoImpl();
        List<Note> notesToDelete = new ArrayList<>();

        try {
            for (String noteid : noteids) {
                //Si hi ha qualsevol nota que no es seva, hauriem de retornar false ja que no podem eliminar-la
                if (!nd.isOwnerNote(userid, Long.parseLong(noteid))) return false;
                if (nd.isOwnerNote(userid, Long.parseLong(noteid))) {
                    notesToDelete.add(nd.getNoteById(Long.parseLong(noteid)));
                }
            }

            nd.delete(notesToDelete);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public long getSharedNoteId(long noteid) {
        NoteDao nd = new NoteDaoImpl();
        try {
            return nd.getSharedNoteId(noteid);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<RenderableNote> getSharedNoteWithMe(long userid, int offset) {
        List<RenderableNote> renderableNotes = new ArrayList<>();
        NoteDao nd = new NoteDaoImpl();
        try {
            List<Note> sharedNotesWithMe = nd.getSharedNotesWithMe(userid, LIMIT, offset);
            for (Note n : sharedNotesWithMe) {
                renderableNotes.add(new RenderableNote(n.getNoteid(), n.getUser(), null, n.getTitle(), n.getBody(), n.getCreationDate(), n.getLastModification()));
            }
            return renderableNotes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<RenderableNote> getSharedNotes(long userid, int offset) {
        List<RenderableNote> renderableNotes = new ArrayList<>();
        NoteDao nd = new NoteDaoImpl();
        UserDao ud = new UserDaoImpl();
        try {
            List<Note> sharedNotes = nd.getSharedNotes(userid, LIMIT, offset);
            for (Note n : sharedNotes) {
                List<User> sharedUsersFromNote = ud.getUsersFromSharedNote(n.getNoteid());
                renderableNotes.add(new RenderableNote(n.getNoteid(), n.getUser(), sharedUsersFromNote, n.getTitle(), n.getBody(), n.getCreationDate(), n.getLastModification()));
            }
            return renderableNotes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public long getLengthSharedNoteWithMe(long userid) {
        NoteDao nd = new NoteDaoImpl();
        try {
            return nd.getSharedNotesWithMeLength(userid);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public long getLengthSharedNotes(long userid) {
        NoteDao nd = new NoteDaoImpl();
        try {
            return nd.getSharedNotesLength(userid);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public boolean shareNote(long userWhoShares, long noteid, String[] usernames) {
        NoteDao nd = new NoteDaoImpl();
        UserDao ud = new UserDaoImpl();

        try {
            Note noteForShare = nd.getNoteById(noteid);

            List<User> users = new ArrayList<>();
            //Si no existeix, ni entrarem al bucle
            if (noteForShare != null) {
                for (String username : usernames) {
                    long userid = ud.getUserIdByUsername(username);
                    //Per guardar l'usuari al que compartirem, abans hem de comprovar si la nota que es vol compartir ja ho esta amb els usuaris introduits. Si no ho esta ho afegim
                    //a la llista
                    if (!nd.sharedNoteExists(userid, noteid)) {
                        User user = ud.getUserById(userid);
                        users.add(user);
                    }
                }

                //Mentres hagui usuaris a la llista, crearem el share
                if (users.size() != 0) {
                    nd.createShare(noteForShare, users);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public boolean deleteShareNote(long userWhoDeleteShares, long noteid, String[] usernames) {
        NoteDao nd = new NoteDaoImpl();
        UserDao ud = new UserDaoImpl();
        try {
            Note noteForDeleteShare = nd.getNoteById(noteid);
            List<User> users = new ArrayList<>();
            if (noteForDeleteShare != null) {
                for (String username : usernames) {
                    long userid = ud.getUserIdByUsername(username);
                    //Per guardar l'usuari al que descompartirem, abans hem de comprovar si la nota que es vol descompartir ja ho esta compartida previamente amb els usuaris introduits.
                    //Si esta compartida, ho afegim a la llista
                    if (nd.sharedNoteExists(userid, noteid)) {
                        User user = ud.getUserById(userid);
                        users.add(user);
                    }
                }
                if (users.size() != 0) {
                    nd.deleteShare(noteForDeleteShare, users);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    @Override
    public boolean deleteAllShareNote(long userid, long noteid) {
        NoteDao nd = new NoteDaoImpl();
        try {
            List<Note> sharedNotes = nd.getSharedNotes(userid, (int) nd.getSharedNotesLength(userid), 0);
            List<Note> sharedWithMe = nd.getSharedNotesWithMe(userid, (int) nd.getSharedNotesWithMeLength(userid), 0);
            boolean canDelete = false;

            //Primer comprovam que la nota que esta compartida es troba a les notes compartides "With Me"
            for (Note n : sharedWithMe) {
                if (n.getNoteid() == noteid) {
                    canDelete = true;
                }
            }

            //Si la nota esta compartida amb tu i nomes amb tu, si podem pasar a borrar la nota
            if (canDelete) {
                nd.deleteAllSharesByNoteId(noteid);
                return true;
            }

            //Si la nota no ha estat compartida amb mi, farem una segona comprovacio amb les notes que jo he compartides
            for (Note n : sharedNotes) {
                if (n.getUser().getUserid() == userid && n.getNoteid() == noteid) {
                    canDelete = true;
                }
            }

            //Si la nota la he compartit jo i nomes jo, podem posar a borrar la nota
            if (canDelete) {
                nd.deleteAllSharesByNoteId(noteid);
                return true;
            }

            //Si arriba fins aqui, voldra dir que intenta esborrar el share d'una nota que ni l'han compartit no ha compartit, per tant no te permis per esborrar-la
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;

    }

    private void parseNoteToRenderable(List<Note> notes, List<RenderableNote> renderableNotes, UserDao ud, List<Note> sharedNotes) throws Exception {
        for (Note allNote : notes) {
            List<User> sharedUsersFromNote = null;
            for (int j = 0; j < sharedNotes.size(); j++) {
                if (sharedNotes.get(j).getNoteid() == allNote.getNoteid()) {
                    sharedUsersFromNote = ud.getUsersFromSharedNote(sharedNotes.get(j).getNoteid());
                    sharedNotes.remove(j);
                    break;
                }
            }
            renderableNotes.add(new RenderableNote(allNote.getNoteid(), allNote.getUser(), sharedUsersFromNote, allNote.getTitle(), allNote.getBody(), allNote.getCreationDate(), allNote.getLastModification()));
        }
    }
}
