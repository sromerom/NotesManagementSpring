package com.liceu.sromerom.services;

import com.liceu.sromerom.entities.Note;
import com.liceu.sromerom.entities.User;
import com.liceu.sromerom.repos.NoteRepo;
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

    @Override
    public List<RenderableNote> getNotesFromUser(long userid, int page) {
        return null;
    }

    @Override
    public List<RenderableNote> getCreatedNotes(long userid, int page) {
        return null;
    }

    @Override
    public long getAllNotesLength(long userid) {
        return 0;
    }

    @Override
    public long getCreatedNotesLength(long userid) {
        return 0;
    }

    @Override
    public List<RenderableNote> filter(long userid, String type, String search, String initDate, String endDate, int page) {
        return null;
    }

    @Override
    public Note getNoteById(long noteid) {
        return null;
    }

    @Override
    public boolean isNoteOwner(long userid, long noteid) {
        return false;
    }

    @Override
    public boolean isSharedNote(long userid, long noteid) {
        return false;
    }

    @Override
    public boolean addNote(long userid, String title, String body) {
        return false;
    }

    @Override
    public boolean editNote(long userid, long noteid, String title, String body) {
        return false;
    }

    @Override
    public boolean deleteNote(long userid, String[] noteids) {
        return false;
    }

    @Override
    public long getSharedNoteId(long noteid) {
        return 0;
    }

    @Override
    public List<RenderableNote> getSharedNoteWithMe(long userid, int page) {
        return null;
    }

    @Override
    public List<RenderableNote> getSharedNotes(long userid, int page) {
        return null;
    }

    @Override
    public long getLengthSharedNoteWithMe(long userid) {
        return 0;
    }

    @Override
    public long getLengthSharedNotes(long userid) {
        return 0;
    }

    @Override
    public boolean shareNote(long userWhoShares, long noteid, String[] usernames) {
        return false;
    }

    @Override
    public boolean deleteShareNote(long userWhoDeleteShare, long noteid, String[] usernames) {
        return false;
    }

    @Override
    public boolean deleteAllShareNote(long userid, long noteid) {
        return false;
    }
}
