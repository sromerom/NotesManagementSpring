package com.liceu.sromerom.repos;

import com.liceu.sromerom.entities.Note;
import com.liceu.sromerom.entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoteRepo extends JpaRepository<Note, Long> {
    //CreatedNotes & Filter
    List<Note> findByUser_Userid(long userid, Pageable pageable);

    @Query(
            value = "SELECT * FROM note INNER JOIN user ON user.userid = note.user_id WHERE user_id = :userid AND (note.title REGEXP :search OR note.body REGEXP :search) AND creationDate BETWEEN :initDate AND :endDate AND NOT note.noteid IN (SELECT sharedNote.note_noteid FROM sharedNote INNER JOIN note ON sharedNote.note_noteid = note.noteid WHERE NOT sharedNote.user_userid = :userid AND (note.title REGEXP :search OR note.body REGEXP :search) AND creationDate BETWEEN :initDate AND :endDate) ORDER BY note.noteid DESC",
            nativeQuery = true)
    List<Note> filterCreatedNotes(@Param("userid") Long userid, @Param("search") String search, @Param("initDate") String initDate, @Param("endDate") String endDate, Pageable pageable);


    long countByUser_Userid(long userid);

    boolean existsNoteByNoteidAndUser_Userid(long noteid, long userid);

    //Filter search in versions
    @Query(
            value = "SELECT note.noteid, note.body, note.creationDate, note.lastModification, note.title, note.user_id FROM note INNER JOIN version ON note.noteid = version.note_id WHERE note.user_id = :userid AND (version.title REGEXP :search OR version.body REGEXP :search) AND version.creationDate BETWEEN :initDate AND :endDate UNION SELECT note.noteid, note.body, note.creationDate, note.lastModification, note.title, note.user_id FROM sharedNote INNER JOIN note ON sharedNote.note_noteid = note.noteid INNER JOIN version ON version.note_id = note.noteid WHERE sharedNote.user_userid = :userid AND (version.title REGEXP :search OR version.body REGEXP :search) AND version.creationDate BETWEEN :initDate AND :endDate",
            nativeQuery = true)
    List<Note> filterNotesByVersion(@Param("userid") Long userid, @Param("search") String search, @Param("initDate") String initDate, @Param("endDate") String endDate, Pageable pageable);

    @Query(
            value = "SELECT * FROM note INNER JOIN user ON user.userid = note.user_id WHERE note.user_id = :userid AND (note.title REGEXP :search OR note.body REGEXP :search) AND creationDate BETWEEN :initDate AND :endDate OR note.noteid IN (SELECT sharedNote.note_noteid FROM sharedNote INNER JOIN note ON sharedNote.note_noteid = note.noteid WHERE sharedNote.user_userid = :userid AND (note.title REGEXP :search OR note.body REGEXP :search) AND creationDate BETWEEN :initDate AND :endDate) ORDER BY note.noteid DESC",
            nativeQuery = true)
    List<Note> filterAllNotes(@Param("userid") Long userid, @Param("search") String search, @Param("initDate") String initDate, @Param("endDate") String endDate, Pageable pageable);


    //############## Filters by title, creation date and last modification DESC & ASC ##############//
    @Query(
            value = "SELECT * FROM note INNER JOIN user ON user.userid = note.user_id WHERE note.user_id = :userid AND (note.title REGEXP :search OR note.body REGEXP :search) AND creationDate BETWEEN :initDate AND :endDate OR note.noteid IN (SELECT sharedNote.note_noteid FROM sharedNote INNER JOIN note ON sharedNote.note_noteid = note.noteid WHERE sharedNote.user_userid = :userid AND (note.title REGEXP :search OR note.body REGEXP :search) AND creationDate BETWEEN :initDate AND :endDate) ORDER BY note.title DESC",
            nativeQuery = true)
    List<Note> filterOrderByTitleDESC(@Param("userid") Long userid, @Param("search") String search, @Param("initDate") String initDate, @Param("endDate") String endDate, Pageable pageable);

    @Query(
            value = "SELECT * FROM note INNER JOIN user ON user.userid = note.user_id WHERE note.user_id = :userid AND (note.title REGEXP :search OR note.body REGEXP :search) AND creationDate BETWEEN :initDate AND :endDate OR note.noteid IN (SELECT sharedNote.note_noteid FROM sharedNote INNER JOIN note ON sharedNote.note_noteid = note.noteid WHERE sharedNote.user_userid = :userid AND (note.title REGEXP :search OR note.body REGEXP :search) AND creationDate BETWEEN :initDate AND :endDate) ORDER BY note.title ASC",
            nativeQuery = true)
    List<Note> filterOrderByTitleASC(@Param("userid") Long userid, @Param("search") String search, @Param("initDate") String initDate, @Param("endDate") String endDate, Pageable pageable);

    @Query(
            value = "SELECT * FROM note INNER JOIN user ON user.userid = note.user_id WHERE note.user_id = :userid AND (note.title REGEXP :search OR note.body REGEXP :search) AND creationDate BETWEEN :initDate AND :endDate OR note.noteid IN (SELECT sharedNote.note_noteid FROM sharedNote INNER JOIN note ON sharedNote.note_noteid = note.noteid WHERE sharedNote.user_userid = :userid AND (note.title REGEXP :search OR note.body REGEXP :search) AND creationDate BETWEEN :initDate AND :endDate) ORDER BY note.creationDate DESC",
            nativeQuery = true)
    List<Note> filterOrderByCreationDateDESC(@Param("userid") Long userid, @Param("search") String search, @Param("initDate") String initDate, @Param("endDate") String endDate, Pageable pageable);

    @Query(
            value = "SELECT * FROM note INNER JOIN user ON user.userid = note.user_id WHERE note.user_id = :userid AND (note.title REGEXP :search OR note.body REGEXP :search) AND creationDate BETWEEN :initDate AND :endDate OR note.noteid IN (SELECT sharedNote.note_noteid FROM sharedNote INNER JOIN note ON sharedNote.note_noteid = note.noteid WHERE sharedNote.user_userid = :userid AND (note.title REGEXP :search OR note.body REGEXP :search) AND creationDate BETWEEN :initDate AND :endDate) ORDER BY note.creationDate ASC",
            nativeQuery = true)
    List<Note> filterOrderByCreationDateASC(@Param("userid") Long userid, @Param("search") String search, @Param("initDate") String initDate, @Param("endDate") String endDate, Pageable pageable);

    @Query(
            value = "SELECT * FROM note INNER JOIN user ON user.userid = note.user_id WHERE note.user_id = :userid AND (note.title REGEXP :search OR note.body REGEXP :search) AND creationDate BETWEEN :initDate AND :endDate OR note.noteid IN (SELECT sharedNote.note_noteid FROM sharedNote INNER JOIN note ON sharedNote.note_noteid = note.noteid WHERE sharedNote.user_userid = :userid AND (note.title REGEXP :search OR note.body REGEXP :search) AND creationDate BETWEEN :initDate AND :endDate) ORDER BY note.lastModification DESC",
            nativeQuery = true)
    List<Note> filterOrderByLastModificationDESC(@Param("userid") Long userid, @Param("search") String search, @Param("initDate") String initDate, @Param("endDate") String endDate, Pageable pageable);

    @Query(
            value = "SELECT * FROM note INNER JOIN user ON user.userid = note.user_id WHERE note.user_id = :userid AND (note.title REGEXP :search OR note.body REGEXP :search) AND creationDate BETWEEN :initDate AND :endDate OR note.noteid IN (SELECT sharedNote.note_noteid FROM sharedNote INNER JOIN note ON sharedNote.note_noteid = note.noteid WHERE sharedNote.user_userid = :userid AND (note.title REGEXP :search OR note.body REGEXP :search) AND creationDate BETWEEN :initDate AND :endDate) ORDER BY note.lastModification ASC",
            nativeQuery = true)
    List<Note> filterOrderByLastModificationASC(@Param("userid") Long userid, @Param("search") String search, @Param("initDate") String initDate, @Param("endDate") String endDate, Pageable pageable);
}
