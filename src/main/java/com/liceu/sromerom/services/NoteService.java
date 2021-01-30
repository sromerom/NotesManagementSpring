package com.liceu.sromerom.services;

import com.liceu.sromerom.entities.Note;
import com.liceu.sromerom.entities.SharedNote;
import com.liceu.sromerom.utils.RenderableNote;

import java.util.List;

public interface NoteService {
    List<RenderableNote> getNotesFromUser(long userid, int page);

    List<RenderableNote> getCreatedNotes(long userid, int page);

    long getAllNotesLength(long userid);

    long getCreatedNotesLength(long userid);

    List<RenderableNote> filter(long userid, String type, String search, String initDate, String endDate, int page);

    Note getNoteById(long noteid);

    boolean isNoteOwner(long userid, long noteid);

    boolean isSharedNote(long userid, long noteid);

    boolean addNote(long userid, String title, String body);

    boolean editNote(long userid, long noteid, String title, String body);

    boolean deleteNote(long userid, String[] noteids);

    //Shared Notes Services

    long getSharedNoteId(long noteid);

    List<RenderableNote> getSharedNoteWithMe(long userid, int page);

    List<RenderableNote> getSharedNotes(long userid, int page);

    long getLengthSharedNoteWithMe(long userid);

    long getLengthSharedNotes(long userid);

    boolean shareNote(long userWhoShares, long noteid, String permissionMode, String[] usernames);

    boolean deleteShareNote(long userWhoDeleteShare, long noteid, String[] usernames);

    boolean deleteAllShareNote(long userid, long noteid);

    boolean hasWritePermission(long userid, long noteid);

    List<SharedNote> getPermissionFromSharedUsers(long noteid);

    boolean updatePermissionMode(long userid, long shareduserid, long noteid, String newPermission);
}
