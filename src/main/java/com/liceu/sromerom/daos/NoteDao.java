package com.liceu.sromerom.daos;

import com.liceu.sromerom.model.Note;
import com.liceu.sromerom.model.User;

import java.util.List;

public interface NoteDao {

    List<Note> getAllNotesFromUser(long userid, int limit, int offset) throws Exception;

    List<Note> getCreatedNotesFromUser(long userid, int limit, int offset) throws Exception;

    long getNotesLengthFromUser(long userid) throws Exception;

    List<Note> filterCreatedNotesBySearch(long userid, String search, int limit, int offset) throws Exception;

    List<Note> filterCreatedNotesByDate(long userid, String initDate, String endDate, int limit, int offset) throws Exception;

    List<Note> filterAllCreatedNotes(long userid, String search, String initDate, String endDate, int limit, int offset) throws Exception;

    List<Note> filterAllTypesOfNoteBySearch(long userid, String search, int limit, int offset) throws Exception;

    List<Note> filterAllTypesOfNoteByDate(long userid, String initDate, String endDate, int limit, int offset) throws Exception;

    List<Note> filterAllTypesOfNoteByAll(long userid, String search, String initDate, String endDate, int limit, int offset) throws Exception;

    Note getNoteById(long noteid) throws Exception;

    boolean isOwnerNote(long userid, long noteid) throws Exception;

    void create(Note note) throws Exception;

    void update(Note note) throws Exception;

    void delete(List<Note> notes) throws Exception;

    //Shared Notes
    List<Note> getSharedNotesWithMe(long userid, int limit, int offset) throws Exception;

    List<Note> getSharedNotes(long userid, int limit, int offset) throws Exception;

    boolean sharedNoteExists(long userid, long noteid) throws Exception;

    long getSharedNoteId(long noteid) throws Exception;

    long getSharedNotesWithMeLength(long userid) throws Exception;

    long getSharedNotesLength(long userid) throws Exception;

    List<Note> filterSharedNotesWithMeBySearch(long userid, String search, int limit, int offset) throws Exception;

    List<Note> filterSharedNotesWithMeByDate(long userid, String initDate, String endDate, int limit, int offset) throws Exception;

    List<Note> filterAllSharedNotesWithMe(long userid, String search, String initDate, String endDate, int limit, int offset) throws Exception;

    List<Note> filterSharedNotesBySearch(long userid, String search, int limit, int offset) throws Exception;

    List<Note> filterSharedNotesByDate(long userid, String initDate, String endDate, int limit, int offset) throws Exception;

    List<Note> filterAllSharedNotes(long userid, String search, String initDate, String endDate, int limit, int offset) throws Exception;

    void createShare(Note noteForShare, List<User> users) throws Exception;

    void deleteShare(Note noteForShare, List<User> users) throws Exception;

    void deleteAllSharesByNoteId(long noteid) throws Exception;

}
