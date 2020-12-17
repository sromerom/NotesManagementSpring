package com.liceu.sromerom.services;

import com.liceu.sromerom.model.Note;
import com.liceu.sromerom.utils.RenderableNote;
//import com.liceu.sromerom.utils.RenderableNote;

import java.util.List;

public interface NoteService {
    List<RenderableNote> getNotesFromUser(long userid, int offset);

    List<RenderableNote> getCreatedNotes(long userid, int offset);

    long getAllNotesLength(long id);

    long getCreatedNotesLength(long id);

    List<RenderableNote> filter(long userid, String type, String search, String initDate, String endDate, int offset);

    Note getNoteById(long noteid);

    boolean isNoteOwner(long userid, long noteid);

    boolean isSharedNote(long userid, long noteid);

    boolean addNote(long userid, String title, String body);

    boolean editNote(long userid, long idnote, String title, String body);

    boolean deleteNote(long userid, String[] noteids);

    //Shared Notes Services

    long getSharedNoteId(long noteid);

    List<RenderableNote> getSharedNoteWithMe(long userid, int offset);

    List<RenderableNote> getSharedNotes(long userid, int offset);

    long getLengthSharedNoteWithMe(long userid);

    long getLengthSharedNotes(long userid);

    boolean shareNote(long userid, long noteid, String[] usernames);

    boolean deleteShareNote(long userid, long noteid, String[] usernames);

    boolean deleteAllShareNote(long userid, long noteid);

}
