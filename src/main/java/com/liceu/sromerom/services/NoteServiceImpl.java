package com.liceu.sromerom.services;

import com.liceu.sromerom.entities.*;
import com.liceu.sromerom.repos.NoteRepo;
import com.liceu.sromerom.repos.SharedNoteRepo;
import com.liceu.sromerom.repos.UserRepo;
import com.liceu.sromerom.repos.VersionRepo;
import com.liceu.sromerom.utils.Filter;
import com.liceu.sromerom.utils.PermissionMode;
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

    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    final static short LIMIT = 10;
    Pageable all = PageRequest.of(0, Integer.SIZE);
    @Autowired
    NoteRepo noteRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    SharedNoteRepo sharedNoteRepo;

    @Autowired
    VersionRepo versionRepo;

    @Override
    public List<RenderableNote> getNotesFromUser(long userid, int page) {
        Pageable topTen = PageRequest.of(page, LIMIT);
        List<RenderableNote> renderableNotes;
        List<SharedNote> sharedNotes = sharedNoteRepo.getSharedNotesByUserid(userid, topTen);

        /*
        List<Note> sharedNotes = sharedNoteRepo.findByNote_User_Userid(userid, topTen)
                .stream().map(a -> a.getNote())
                .collect(Collectors.toList());
         */
        List<Note> allNotes = noteRepo.getAllNotesFromUser(userid, topTen);

        try {
            renderableNotes = parseNoteToRenderable(allNotes, sharedNotes, userid);
            return renderableNotes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public List<RenderableNote> getCreatedNotes(long userid, int page) {
        Pageable limitPage;
        if (page == -1) {
            limitPage = all;
        } else {
            limitPage = PageRequest.of(page, LIMIT);
        }
        List<RenderableNote> renderableNotes = new ArrayList<>();
        List<Note> createdNotes = noteRepo.findByUser_Userid(userid, limitPage);

        if (createdNotes != null) {
            createdNotes.forEach(n -> renderableNotes.add(new RenderableNote(n.getNoteid(), n.getUser(), null, n.getTitle(), n.getBody(), n.getCreationDate(), n.getLastModification(), false)));
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
    public List<RenderableNote> filter(long userid, String optionSelect, String search, String initDate, String endDate, int page) {
        if (optionSelect == null) optionSelect = "";
        if (search == null) search = "";
        if (initDate == null) initDate = "";
        if (endDate == null) endDate = "";

        System.out.println("optionSelect: " + optionSelect);
        System.out.println("search: " + search);
        System.out.println("initDate " + initDate);
        System.out.println("endDate: " + endDate);

        List<Note> notes;
        try {

            Pageable limitPage;
            if (page == -1) {
                limitPage = all;
            } else {
                limitPage = PageRequest.of(page, LIMIT);
            }

            if (!Filter.checkTypeFilter(search, initDate, endDate).equals("filterByDate")) {
                System.out.println("Busqueda sin fechas!!!!!!!!!");
                initDate = "1970-01-01 00:00:00";
                endDate = "2021-09-08 23:59:59";
                //endDate = Instant.now().toString();
                System.out.println(endDate);
            } else {
                //ToUniversalTime()
                LocalDateTime initDateLDT = LocalDateTime.parse(initDate, formatter);
                LocalDateTime endDateLDT = LocalDateTime.parse(endDate, formatter);
                initDate = initDateLDT.minusHours(1).toString();
                endDate = endDateLDT.minusHours(1).toString();
            }

            switch (optionSelect) {
                case "sharedNotesWithMe":
                    System.out.println("Filtramos en notas compartidas contigo!!");
                    notes = sharedNoteRepo.filterSharedNotesWithMe(userid, search, initDate, endDate, limitPage)
                            .stream().map(a -> a.getNote())
                            .distinct()
                            .collect(Collectors.toList());
                    break;
                case "sharedNotesByYou":
                    System.out.println("Filtramos en notas compartidas por ti!!");
                    notes = sharedNoteRepo.filterSharedNotes(userid, search, initDate, endDate, limitPage)
                            .stream().map(a -> a.getNote())
                            .distinct()
                            .collect(Collectors.toList());
                    break;
                case "ownerNotes":
                    System.out.println("Filtramos en notas creadas!!");
                    notes = noteRepo.filterCreatedNotes(userid, search, initDate, endDate, limitPage);
                    break;
                case "titleDESC":
                    System.out.println("Filtramos por titulo descendente");
                    notes = noteRepo.filterOrderByTitleDESC(userid, search, initDate, endDate, limitPage);
                    break;
                case "titleASC":
                    System.out.println("Filtramos por titulo ascendente");
                    notes = noteRepo.filterOrderByTitleASC(userid, search, initDate, endDate, limitPage);
                    break;
                case "creationDateDESC":
                    System.out.println("Filtramos por creationDate desc");
                    notes = noteRepo.filterOrderByCreationDateDESC(userid, search, initDate, endDate, limitPage);
                    break;
                case "creationDateASC":
                    System.out.println("Filtramos por creationDate asc");
                    notes = noteRepo.filterOrderByCreationDateASC(userid, search, initDate, endDate, limitPage);
                    break;
                case "lastModificationDESC":
                    System.out.println("Filtramos por lastModification desc");
                    notes = noteRepo.filterOrderByLastModificationDESC(userid, search, initDate, endDate, limitPage);
                    break;
                case "lastModificationASC":
                    System.out.println("Filtramos por lastModification asc");
                    notes = noteRepo.filterOrderByLastModificationASC(userid, search, initDate, endDate, limitPage);
                    break;
                default:
                    optionSelect = "default";
                    System.out.println("Filtramos en todas las notas!!");
                    notes = noteRepo.filterNotesByAll(userid, search, initDate, endDate, limitPage);
                    break;
            }

            /*
            List<Note> notesDistinct = sharedNoteRepo.getSharedNotesByUserid(userid, topTen)
                    .stream().map(a -> a.getNote())
                    .distinct()
                    .collect(Collectors.toList());

             */

            List<SharedNote> sharedNotes = sharedNoteRepo.getSharedNotesByUserid(userid, all);
            System.out.println("sharedNotes" + sharedNoteRepo.getSharedNotesByUserid(userid, all));
            /*
            List<Note> sharedNotes = sharedNoteRepo.findByNote_User_Userid(userid, topTen)
                    .stream().map(a -> a.getNote())
                    .collect(Collectors.toList());

             */
            List<RenderableNote> renderableNotes = parseNoteToRenderable(notes, sharedNotes, userid);
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
        LocalDateTime myDateObj = LocalDateTime.now();
        String dateString = myDateObj.format(formatter);
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
        System.out.println("Esta es la fecha con la que se añadira la nota!!!!!!!!!!!!!!!!");
        System.out.println(dateTime.toString());

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
        LocalDateTime myDateObj = LocalDateTime.now();
        String dateString = myDateObj.format(formatter);
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);


        //Owner = sromerom = 1 & note = 1
        //Shared = pnegre = 2 & note = 1
        if (isNoteOwner(userid, noteid) || hasWritePermission(userid, noteid)) {
            Version addVersion = new Version();
            Note noteToUpdate = noteRepo.findById(noteid).get();
            User whoUpdate = userRepo.findById(userid).get();

            //Afegim la info actual de la nota a una versio nova
            addVersion.setTitle(noteToUpdate.getTitle());
            addVersion.setBody(noteToUpdate.getBody());
            addVersion.setCreationDate(dateTime);
            addVersion.setNote(noteToUpdate);
            addVersion.setUser(whoUpdate);
            Version versionAdded = versionRepo.save(addVersion);

            if (versionAdded != null) {
                //I quan s'ha guardat l'anterior info, editam l'actual nota amb la info nova
                noteToUpdate.setTitle(title);
                noteToUpdate.setBody(body);
                noteToUpdate.setLastModification(dateTime);
                Note updatedNote = noteRepo.save(noteToUpdate);
                if (updatedNote != null) return true;
            }
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
            if (sharedNoteRepo.existsByNote_Noteid(n.getNoteid())) {
                List<String> listUsernames = new ArrayList<>();
                userRepo.getUsersFromSharedNote(n.getNoteid())
                        .stream()
                        .map(ne -> listUsernames.add(ne.getUsername()))
                        .collect(Collectors.toList());
                String[] usernamesToDeleteShare = new String[listUsernames.size()];
                listUsernames.toArray(usernamesToDeleteShare);
                deleteShareNote(userid, n.getNoteid(), usernamesToDeleteShare);
            }
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
        List<SharedNote> sharedNotesWithMe = sharedNoteRepo.findByUser_Userid(userid, topTen);

        boolean writeable = false;
        for (SharedNote sh : sharedNotesWithMe) {
            if (sh.getPermissionMode().equals(PermissionMode.WRITEMODE)) writeable = true;
            renderableNotes.add(new RenderableNote(sh.getNote().getNoteid(), sh.getNote().getUser(), null, sh.getNote().getTitle(), sh.getNote().getBody(), sh.getNote().getCreationDate(), sh.getNote().getLastModification(), writeable));
            writeable = false;
        }

        return renderableNotes;
    }

    @Override
    public List<RenderableNote> getSharedNotes(long userid, int page) {
        List<RenderableNote> renderableNotes = new ArrayList<>();
        Pageable topTen = PageRequest.of(page, LIMIT);
        List<Note> sharedNotes = sharedNoteRepo.getSharedNotesByUserid(userid, topTen)
                .stream().map(a -> a.getNote())
                .distinct()
                .collect(Collectors.toList());


        for (Note n : sharedNotes) {
            List<User> sharedUsersFromNote = noteRepo.getUsersFromSharedNote(n.getNoteid());
            renderableNotes.add(new RenderableNote(n.getNoteid(), n.getUser(), sharedUsersFromNote, n.getTitle(), n.getBody(), n.getCreationDate(), n.getLastModification(), false));
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
    public boolean shareNote(long userWhoShares, long noteid, String permissionMode, String[] usernames) {
        Note noteForShare = noteRepo.findById(noteid).get();
        List<User> usersToShare = new ArrayList<>();
        PermissionMode permissionModeEnum = null;

        System.out.println("Los permisos que quiere...");
        System.out.println(permissionMode + " =? " + PermissionMode.READMODE.toString());
        if (permissionMode.equalsIgnoreCase(PermissionMode.READMODE.toString())) {
            permissionModeEnum = PermissionMode.READMODE;
        }
        if (permissionMode.equalsIgnoreCase(PermissionMode.WRITEMODE.toString())) {
            permissionModeEnum = PermissionMode.WRITEMODE;
        }

        if (permissionMode == null) return false;


        //Si no existeix, ni entrarem al bucle
        if (noteForShare != null) {
            for (String username : usernames) {
                User userToShare = userRepo.findUserByUsername(username);
                //Per guardar l'usuari al que compartirem, abans hem de comprovar si la nota que es vol compartir ja ho esta amb els usuaris introduits. Si no ho esta ho afegim
                //a la llista

                //Si entre els usuaris trobam el usuari owner de la nota, no compartirem la nota
                if (noteRepo.findNoteByNoteidAndUser_Userid(noteid, userToShare.getUserid()) != null) {
                    return false;
                }
                if (sharedNoteRepo.findByUser_UseridAndNote_Noteid(userToShare.getUserid(), noteid) == null) {
                    User user = userRepo.findById(userToShare.getUserid()).get();
                    usersToShare.add(user);
                }
            }

            //Mentres hagui usuaris a la llista, crearem el share
            if (usersToShare.size() != 0) {
                for (User u : usersToShare) {
                    User user = userRepo.findById(u.getUserid()).get();
                    SharedNote newSharedNote = new SharedNote();
                    newSharedNote.setPermissionMode(permissionModeEnum);
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
        List<Note> sharedNotes = sharedNoteRepo.getSharedNotesByUserid(userid, all)
                .stream().map(a -> a.getNote())
                .distinct()
                .collect(Collectors.toList());
        List<Note> sharedNotesWithMe = sharedNoteRepo.findByUser_Userid(userid, all)
                .stream().map(a -> a.getNote())
                .collect(Collectors.toList());


        boolean canDelete = false;

        //Primer comprovam que la nota que esta compartida es troba a les notes compartides "With Me"
        for (Note n : sharedNotesWithMe) {
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
        for (Note n : sharedNotes) {
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

    @Override
    public boolean hasWritePermission(long userid, long noteid) {
        SharedNote sharedNote = sharedNoteRepo.findByUser_UseridAndNote_Noteid(userid, noteid);
        if (sharedNote.getPermissionMode().equals(PermissionMode.WRITEMODE)) return true;
        return false;
    }

    @Override
    public List<SharedNote> getPermissionFromSharedUsers(long noteid) {
        List<SharedNote> permissions = new ArrayList<>();
        List<User> users = userRepo.getUsersFromSharedNote(noteid);
        for (User user : users) {
            SharedNote sharedNote = sharedNoteRepo.findByUser_UseridAndNote_Noteid(user.getUserid(), noteid);
            permissions.add(sharedNote);
        }

        return permissions;
    }

    @Override
    public boolean updatePermissionMode(long userid, long shareduserid, long noteid, String newPermission) {
        PermissionMode permissionMode = null;
        if (newPermission.equalsIgnoreCase(PermissionMode.READMODE.toString())) {
            permissionMode = PermissionMode.READMODE;
        }
        if (newPermission.equalsIgnoreCase(PermissionMode.WRITEMODE.toString())) {
            permissionMode = PermissionMode.WRITEMODE;
        }

        if (permissionMode == null) return false;

        SharedNote shareNoteToUpdate = sharedNoteRepo.findByUser_UseridAndNote_Noteid(shareduserid, noteid);
        shareNoteToUpdate.setPermissionMode(permissionMode);
        SharedNote updateSharedNote = sharedNoteRepo.save(shareNoteToUpdate);
        if (updateSharedNote != null) return true;

        return false;
    }

    private List<RenderableNote> parseNoteToRenderable(List<Note> allNotes, List<SharedNote> sharedNotes, long userid) throws Exception {
        List<RenderableNote> result = new ArrayList<>();
        for (Note note : allNotes) {
            List<User> sharedUsersFromNote = null;
            for (SharedNote sh : sharedNotes) {
                if (sh.getNote().getNoteid().equals(note.getNoteid())) {
                    sharedUsersFromNote = userRepo.getUsersFromSharedNote(sh.getNote().getNoteid());
                    sharedNotes.remove(0);
                    break;
                }
            }

            boolean writeable = false;
            SharedNote sh = sharedNoteRepo.findByUser_UseridAndNote_Noteid(userid, note.getNoteid());

            if (sharedNoteRepo.existsByNote_Noteid(note.getNoteid()) && sh != null) {
                if (sh.getPermissionMode().equals(PermissionMode.WRITEMODE)) writeable = true;
            }

            result.add(new RenderableNote(note.getNoteid(), note.getUser(), sharedUsersFromNote, note.getTitle(), note.getBody(), note.getCreationDate(), note.getLastModification(), writeable));

        }
        return result;
    }
}
