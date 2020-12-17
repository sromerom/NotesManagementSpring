package com.liceu.sromerom.daos;

import com.liceu.sromerom.model.Note;
import com.liceu.sromerom.model.User;
import com.liceu.sromerom.utils.Database;
import jdk.vm.ci.meta.Local;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NoteDaoImpl implements NoteDao {
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<Note> getAllNotesFromUser(long userid, int limit, int offset) throws Exception {
        List<Note> result = new ArrayList<>();
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT note_id, title, body, creationDate, lastModificationDate, user_id, email, username, password FROM note INNER JOIN user ON user.user_id = note.user_iduser WHERE note.user_iduser = ? OR note.note_id IN (SELECT sharedNote.note_id FROM sharedNote INNER JOIN note ON sharedNote.note_id = note.note_id WHERE sharedNote.user_id = ?) ORDER BY note.note_id DESC LIMIT ? OFFSET ?");
        ps.setLong(1, userid);
        ps.setLong(2, userid);
        ps.setInt(3, limit);
        ps.setInt(4, offset);
        ResultSet rs = ps.executeQuery();
        makeNote(result, rs);
        ps.close();
        return result;
    }

    @Override
    public List<Note> getCreatedNotesFromUser(long userid, int limit, int offset) throws Exception {
        List<Note> result = new ArrayList<>();
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT note_id, title, body, creationDate, lastModificationDate, user_id, email, username, password FROM note INNER JOIN user ON note.user_iduser = user.user_id WHERE user_iduser = ? ORDER BY note.note_id DESC LIMIT ? OFFSET ?");
        ps.setLong(1, userid);
        ps.setInt(2, limit);
        ps.setInt(3, offset);
        ResultSet rs = ps.executeQuery();
        makeNote(result, rs);
        ps.close();
        return result;
    }

    @Override
    public long getNotesLengthFromUser(long userid) throws Exception {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT COUNT(note_id) FROM note WHERE user_iduser = ?");
        ps.setLong(1, userid);
        ResultSet rs = ps.executeQuery();
        long totalNotes = rs.getInt(1);
        ps.close();
        return totalNotes;
    }

    @Override
    public List<Note> filterCreatedNotesBySearch(long userid, String search, int limit, int offset) throws Exception {
        List<Note> result = new ArrayList<>();
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT note_id, title, body, creationDate, lastModificationDate, user_id, email, username, password FROM note INNER JOIN user ON user.user_id = note.user_iduser WHERE user_iduser = ? AND (note.title LIKE ? OR note.body LIKE ?) ORDER BY note.note_id DESC LIMIT ? OFFSET ?");
        ps.setLong(1, userid);
        ps.setString(2, "%" + search + "%");
        ps.setString(3, "%" + search + "%");
        ps.setInt(4, limit);
        ps.setInt(5, offset);
        ResultSet rs = ps.executeQuery();
        makeNote(result, rs);
        ps.close();
        return result;
    }

    @Override
    public List<Note> filterCreatedNotesByDate(long userid, String initDate, String endDate, int limit, int offset) throws Exception {
        List<Note> result = new ArrayList<>();
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT note_id, title, body, creationDate, lastModificationDate, user_id, email, username, password FROM note INNER JOIN user ON user.user_id = note.user_iduser WHERE user_iduser = ? AND creationDate > ? AND lastModificationDate < ? ORDER BY note.note_id DESC LIMIT ? OFFSET ?");
        ps.setLong(1, userid);
        ps.setString(2, initDate);
        ps.setString(3, endDate);
        ps.setInt(4, limit);
        ps.setInt(5, offset);
        ResultSet rs = ps.executeQuery();
        makeNote(result, rs);
        ps.close();
        return result;
    }

    @Override
    public List<Note> filterAllCreatedNotes(long userid, String search, String initDate, String endDate, int limit, int offset) throws Exception {
        List<Note> result = new ArrayList<>();
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT note_id, title, body, creationDate, lastModificationDate, user_id, email, username, password FROM note INNER JOIN user ON user.user_id = note.user_iduser WHERE user_iduser = ? AND (note.title LIKE ? OR note.body LIKE ?) AND creationDate > ? AND lastModificationDate < ? ORDER BY note.note_id DESC LIMIT ? OFFSET ?");
        ps.setLong(1, userid);
        ps.setString(2, "%" + search + "%");
        ps.setString(3, "%" + search + "%");
        ps.setString(4, initDate);
        ps.setString(5, endDate);
        ps.setInt(6, limit);
        ps.setInt(7, offset);
        ResultSet rs = ps.executeQuery();
        makeNote(result, rs);
        ps.close();
        return result;
    }

    @Override
    public List<Note> filterAllTypesOfNoteBySearch(long userid, String search, int limit, int offset) throws Exception {
        List<Note> result = new ArrayList<>();
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT note_id, title, body, creationDate, lastModificationDate, user_id, email, username, password FROM note INNER JOIN user ON user.user_id = note.user_iduser WHERE note.user_iduser = ? AND (note.title LIKE ? OR note.body LIKE ?) OR note.note_id IN (SELECT sharedNote.note_id FROM sharedNote INNER JOIN note ON sharedNote.note_id = note.note_id WHERE sharedNote.user_id = ? AND (note.title LIKE ? OR note.body LIKE ?)) ORDER BY note.note_id DESC LIMIT ? OFFSET ?");
        ps.setLong(1, userid);
        ps.setString(2, "%" + search + "%");
        ps.setString(3, "%" + search + "%");
        ps.setLong(4, userid);
        ps.setString(5, "%" + search + "%");
        ps.setString(6, "%" + search + "%");
        ps.setInt(7, limit);
        ps.setInt(8, offset);
        ResultSet rs = ps.executeQuery();
        makeNote(result, rs);
        ps.close();
        return result;
    }

    @Override
    public List<Note> filterAllTypesOfNoteByDate(long userid, String initDate, String endDate, int limit, int offset) throws Exception {
        List<Note> result = new ArrayList<>();
        Connection conn = Database.getConnection();
        //PreparedStatement ps = conn.prepareStatement("SELECT note_id, title, body, creationDate, lastModificationDate, email, username, password FROM note INNER JOIN user ON user.user_id = note.user_iduser WHERE note.user_iduser = ? AND creationDate > ? AND lastModificationDate < ? OR note.note_id IN (SELECT sharedNote.note_id FROM sharedNote INNER JOIN note ON sharedNote.note_id = note.note_id WHERE sharedNote.user_id = ?) ORDER BY note.note_id DESC LIMIT ? OFFSET ?");
        PreparedStatement ps = conn.prepareStatement("SELECT note_id, title, body, creationDate, lastModificationDate, user_id, email, username, password FROM note INNER JOIN user ON user.user_id = note.user_iduser WHERE note.user_iduser = ? AND creationDate > ? AND lastModificationDate < ? OR note.note_id IN (SELECT sharedNote.note_id FROM sharedNote INNER JOIN note ON sharedNote.note_id = note.note_id WHERE sharedNote.user_id = ? AND creationDate > ? AND lastModificationDate < ?) ORDER BY note.note_id DESC LIMIT ? OFFSET ?");
        ps.setLong(1, userid);
        ps.setString(2, initDate);
        ps.setString(3, endDate);
        ps.setLong(4, userid);
        ps.setString(5, initDate);
        ps.setString(6, endDate);
        ps.setInt(7, limit);
        ps.setInt(8, offset);
        ResultSet rs = ps.executeQuery();
        makeNote(result, rs);
        ps.close();
        return result;
    }

    @Override
    public List<Note> filterAllTypesOfNoteByAll(long userid, String search, String initDate, String endDate, int limit, int offset) throws Exception {
        List<Note> result = new ArrayList<>();
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT note_id, title, body, creationDate, lastModificationDate, user_id, email, username, password FROM note INNER JOIN user ON user.user_id = note.user_iduser WHERE note.user_iduser = ? AND (note.title LIKE ? OR note.body LIKE ?) AND creationDate > ? AND lastModificationDate < ? OR note.note_id IN (SELECT sharedNote.note_id FROM sharedNote INNER JOIN note ON sharedNote.note_id = note.note_id WHERE sharedNote.user_id = ? AND (note.title LIKE ? OR note.body LIKE ?) AND creationDate > ? AND lastModificationDate < ?) ORDER BY note.note_id DESC LIMIT ? OFFSET ?");
        ps.setLong(1, userid);
        ps.setString(2, "%" + search + "%");
        ps.setString(3, "%" + search + "%");
        ps.setString(4, initDate);
        ps.setString(5, endDate);
        ps.setLong(6, userid);
        ps.setString(7, "%" + search + "%");
        ps.setString(8, "%" + search + "%");
        ps.setString(9, endDate);
        ps.setLong(10, userid);
        ps.setInt(11, limit);
        ps.setInt(12, offset);
        ResultSet rs = ps.executeQuery();
        makeNote(result, rs);
        ps.close();
        return result;
    }

    @Override
    public Note getNoteById(long noteid) throws Exception {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT note_id, title, body, creationDate, lastModificationDate, user_id, email, username, password FROM note INNER JOIN user ON user.user_id = note.user_iduser WHERE note_id = ?");
        ps.setLong(1, noteid);
        ResultSet rs = ps.executeQuery();
        long userid = rs.getLong(1);
        String actualTitle = rs.getString(2);
        String body = rs.getString(3);
        LocalDateTime creationDate = LocalDateTime.parse(rs.getString(4),formatter);
        LocalDateTime lastModificationDate = LocalDateTime.parse(rs.getString(5), formatter);
        String email = rs.getString(6);
        String username = rs.getString(7);
        String password = rs.getString(8);
        rs.close();
        ps.close();
        return new Note(noteid, new User(userid, email, username, password), actualTitle, body, creationDate, lastModificationDate);
    }

    @Override
    public boolean isOwnerNote(long userid, long noteid) throws Exception {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT title, body, creationDate, lastModificationDate, email, username, password FROM note INNER JOIN user ON user.user_id = note.user_iduser WHERE user_iduser = ? AND note_id = ?");
        ps.setLong(1, userid);
        ps.setLong(2, noteid);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    @Override
    public void create(Note note) throws Exception {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO note (user_iduser, title, body, creationDate, lastModificationDate) VALUES (?,?,?,?,?)");
        ps.setLong(1, note.getUser().getUserid());
        ps.setString(2, note.getTitle());
        ps.setString(3, note.getBody());
        ps.setString(4, formatter.format(note.getCreationDate()));
        ps.setString(5, formatter.format(note.getLastModification()));
        ps.execute();
        ps.close();

    }

    @Override
    public void update(Note note) throws Exception {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE note SET title = ?, body = ?, lastModificationDate = ? WHERE note_id = ?");
        ps.setString(1, note.getTitle());
        ps.setString(2, note.getBody());
        ps.setString(3, formatter.format(note.getLastModification()));
        ps.setLong(4, note.getNoteid());
        ps.execute();
        ps.close();
    }

    @Override
    public void delete(List<Note> notes) throws Exception {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM note WHERE note_id = ?");
        for (Note n : notes) {
            ps.setLong(1, n.getNoteid());
            ps.execute();
        }
        ps.close();
    }

    // Shared Notes operations
    @Override
    public List<Note> getSharedNotesWithMe(long userid, int limit, int offset) throws SQLException {
        List<Note> result = new ArrayList<>();
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT sharedNote.note_id, title, body, creationDate, lastModificationDate, user_iduser, email, username, password FROM sharedNote INNER JOIN note ON sharedNote.note_id = note.note_id INNER JOIN user ON note.user_iduser = user.user_id WHERE sharedNote.user_id = ? ORDER BY sharedNote.shared_note DESC LIMIT ? OFFSET ?");
        ps.setLong(1, userid);
        ps.setInt(2, limit);
        ps.setInt(3, offset);
        ResultSet rs = ps.executeQuery();
        makeNote(result, rs);
        ps.close();
        return result;
    }

    @Override
    public List<Note> getSharedNotes(long userid, int limit, int offset) throws Exception {
        List<Note> result = new ArrayList<>();
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT DISTINCT sharedNote.note_id, title, body, creationDate, lastModificationDate, user_iduser, email, username, password FROM sharedNote INNER JOIN note ON sharedNote.note_id = note.note_id INNER JOIN user ON note.user_iduser = user.user_id WHERE note.user_iduser = ? ORDER BY sharedNote.shared_note DESC LIMIT ? OFFSET ?");
        ps.setLong(1, userid);
        ps.setInt(2, limit);
        ps.setInt(3, offset);
        ResultSet rs = ps.executeQuery();
        makeNote(result, rs);
        ps.close();
        return result;
    }

    @Override
    public boolean sharedNoteExists(long userid, long noteid) throws Exception {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM sharedNote INNER JOIN user ON sharedNote.user_id = user.user_id WHERE user.user_id = ? AND note_id = ?");
        ps.setLong(1, userid);
        ps.setLong(2, noteid);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    @Override
    public long getSharedNoteId(long noteid) throws Exception {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT shared_note FROM sharedNote WHERE note_id = ?");
        ps.setLong(1, noteid);
        ResultSet rs = ps.executeQuery();
        long sharedNoteId = rs.getLong(1);
        ps.close();
        return sharedNoteId;
    }

    @Override
    public long getSharedNotesWithMeLength(long userid) throws Exception {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT COUNT(shared_note) FROM sharedNote WHERE user_id = ?");
        ps.setLong(1, userid);
        ResultSet rs = ps.executeQuery();
        long totalNotes = rs.getLong(1);
        ps.close();
        return totalNotes;
    }

    @Override
    public long getSharedNotesLength(long userid) throws Exception {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement(" SELECT COUNT(user_iduser) FROM sharedNote INNER JOIN note ON note.note_id = sharedNote.note_id WHERE user_iduser = ?;");
        ps.setLong(1, userid);
        ResultSet rs = ps.executeQuery();
        long totalNotes = rs.getLong(1);
        ps.close();
        return totalNotes;
    }

    @Override
    public List<Note> filterSharedNotesWithMeBySearch(long userid, String search, int limit, int offset) throws Exception {
        List<Note> result = new ArrayList<>();
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT DISTINCT sharedNote.note_id, title, body, creationDate, lastModificationDate, user_iduser, email, username, password FROM sharedNote INNER JOIN note ON sharedNote.note_id = note.note_id INNER JOIN user ON note.user_iduser = user.user_id WHERE sharedNote.user_id = ? AND (note.title LIKE ? OR note.body LIKE ?) ORDER BY sharedNote.shared_note DESC LIMIT ? OFFSET ?");
        ps.setLong(1, userid);
        ps.setString(2, "%" + search + "%");
        ps.setString(3, "%" + search + "%");
        ps.setInt(4, limit);
        ps.setInt(5, offset);
        ResultSet rs = ps.executeQuery();
        makeNote(result, rs);
        ps.close();
        return result;
    }

    @Override
    public List<Note> filterSharedNotesWithMeByDate(long userid, String initDate, String endDate, int limit, int offset) throws Exception {
        List<Note> result = new ArrayList<>();
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT DISTINCT sharedNote.note_id, title, body, creationDate, lastModificationDate, user_iduser, email, username, password FROM sharedNote INNER JOIN note ON sharedNote.note_id = note.note_id INNER JOIN user ON note.user_iduser = user.user_id WHERE sharedNote.user_id = ? AND creationDate > ? AND lastModificationDate < ? ORDER BY sharedNote.shared_note DESC LIMIT ? OFFSET ?");
        ps.setLong(1, userid);
        ps.setString(2, initDate);
        ps.setString(3, endDate);
        ps.setInt(4, limit);
        ps.setInt(5, offset);
        ResultSet rs = ps.executeQuery();
        makeNote(result, rs);
        ps.close();
        return result;
    }

    @Override
    public List<Note> filterAllSharedNotesWithMe(long userid, String search, String initDate, String endDate, int limit, int offset) throws Exception {
        List<Note> result = new ArrayList<>();
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT DISTINCT sharedNote.note_id, title, body, creationDate, lastModificationDate, user_iduser, email, username, password FROM sharedNote INNER JOIN note ON sharedNote.note_id = note.note_id INNER JOIN user ON note.user_iduser = user.user_id WHERE sharedNote.user_id = ? AND (note.title LIKE ? OR note.body LIKE ?) AND creationDate > ? AND lastModificationDate < ? ORDER BY sharedNote.shared_note DESC LIMIT ? OFFSET ?");
        ps.setLong(1, userid);
        ps.setString(2, "%" + search + "%");
        ps.setString(3, "%" + search + "%");
        ps.setString(4, initDate);
        ps.setString(5, endDate);
        ps.setInt(6, limit);
        ps.setInt(7, offset);
        ResultSet rs = ps.executeQuery();
        makeNote(result, rs);
        ps.close();
        return result;
    }

    @Override
    public List<Note> filterSharedNotesBySearch(long userid, String search, int limit, int offset) throws Exception {
        List<Note> result = new ArrayList<>();
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT DISTINCT sharedNote.note_id, title, body, creationDate, lastModificationDate, user_iduser, email, username, password FROM sharedNote INNER JOIN note ON sharedNote.note_id = note.note_id INNER JOIN user ON note.user_iduser = user.user_id WHERE note.user_iduser = ? AND (note.title LIKE ? OR note.body LIKE ?) ORDER BY sharedNote.shared_note DESC LIMIT ? OFFSET ?");
        ps.setLong(1, userid);
        ps.setString(2, "%" + search + "%");
        ps.setString(3, "%" + search + "%");
        ps.setInt(4, limit);
        ps.setInt(5, offset);
        ResultSet rs = ps.executeQuery();
        makeNote(result, rs);
        ps.close();
        return result;
    }

    @Override
    public List<Note> filterSharedNotesByDate(long userid, String initDate, String endDate, int limit, int offset) throws Exception {
        List<Note> result = new ArrayList<>();
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT DISTINCT sharedNote.note_id, title, body, creationDate, lastModificationDate, user_iduser, email, username, password FROM sharedNote INNER JOIN note ON sharedNote.note_id = note.note_id INNER JOIN user ON note.user_iduser = user.user_id WHERE note.user_iduser = ? AND creationDate > ? AND lastModificationDate < ? ORDER BY sharedNote.shared_note DESC LIMIT ? OFFSET ?");
        ps.setLong(1, userid);
        ps.setString(2, initDate);
        ps.setString(3, endDate);
        ps.setInt(4, limit);
        ps.setInt(5, offset);
        ResultSet rs = ps.executeQuery();
        makeNote(result, rs);
        ps.close();
        return result;
    }

    @Override
    public List<Note> filterAllSharedNotes(long userid, String search, String initDate, String endDate, int limit, int offset) throws Exception {
        List<Note> result = new ArrayList<>();
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT DISTINCT sharedNote.note_id, title, body, creationDate, lastModificationDate, user_iduser, email, username, password FROM sharedNote INNER JOIN note ON sharedNote.note_id = note.note_id INNER JOIN user ON note.user_iduser = user.user_id WHERE note.user_iduser = ? AND (note.title LIKE ? OR note.body LIKE ?) AND creationDate > ? AND lastModificationDate < ? ORDER BY sharedNote.shared_note DESC LIMIT ? OFFSET ?");
        ps.setLong(1, userid);
        ps.setString(2, "%" + search + "%");
        ps.setString(3, "%" + search + "%");
        ps.setString(4, initDate);
        ps.setString(5, endDate);
        ps.setInt(6, limit);
        ps.setInt(7, offset);
        ResultSet rs = ps.executeQuery();
        makeNote(result, rs);
        ps.close();
        return result;
    }

    @Override
    public void createShare(Note noteForShare, List<User> users) throws Exception {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO sharedNote (note_id, user_id) VALUES (?,?)");
        for (User user : users) {
            ps.setLong(1, noteForShare.getNoteid());
            ps.setLong(2, user.getUserid());
            ps.execute();
        }
        ps.close();
    }

    @Override
    public void deleteShare(Note noteForShare, List<User> users) throws Exception {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM sharedNote WHERE note_id = ? AND user_id = ?");
        for (User user : users) {
            ps.setLong(1, noteForShare.getNoteid());
            ps.setLong(2, user.getUserid());
            ps.execute();
        }
        ps.close();
    }

    @Override
    public void deleteAllSharesByNoteId(long noteid) throws Exception {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM sharedNote WHERE note_id = ?");
        ps.setLong(1, noteid);
        ps.execute();
        ps.close();
    }

    private void makeNote(List<Note> result, ResultSet rs) throws SQLException {
        while (rs.next()) {
            long noteid = rs.getLong(1);
            String actualTitle = rs.getString(2);
            String body = rs.getString(3);
            LocalDateTime creationDate = LocalDateTime.parse(rs.getString(4),formatter);
            LocalDateTime lastModificationDate = LocalDateTime.parse(rs.getString(5), formatter);
            long useridNote = rs.getLong(6);
            String email = rs.getString(7);
            String username = rs.getString(8);
            String password = rs.getString(9);

            Note note = new Note(noteid, new User(useridNote, email, username, password), actualTitle, body, creationDate, lastModificationDate);
            result.add(note);
        }
    }
}


