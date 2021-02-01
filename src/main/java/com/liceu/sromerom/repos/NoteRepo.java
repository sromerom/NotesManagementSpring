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

    //@Query("from note n inner join n.user u where n.user.userid = :userid and (n.title like %:search% or note.body like %:body%) and n.creationDate BETWEEN :startDate and :endDate")
    @Query(
            value = "SELECT * FROM note INNER JOIN user ON user.userid = note.user_id WHERE user_id = :userid AND (note.title LIKE %:search% OR note.body LIKE %:search%) AND creationDate BETWEEN :initDate AND :endDate AND NOT note.noteid IN (SELECT sharedNote.note_noteid FROM sharedNote INNER JOIN note ON sharedNote.note_noteid = note.noteid WHERE NOT sharedNote.user_userid = :userid AND (note.title LIKE %:search% OR note.body LIKE %:search%) AND creationDate BETWEEN :initDate AND :endDate) ORDER BY note.noteid DESC",
            nativeQuery = true)
    List<Note> filterCreatedNotes(@Param("userid") Long userid, @Param("search") String search, @Param("initDate") String initDate, @Param("endDate") String endDate, Pageable pageable);


    //LengthCreatedNotes
    long countByUser_Userid(long userid);

    //Is owner note?
    Note findNoteByNoteidAndUser_Userid(long noteid, long userid);

    @Query("from user u inner join u.sharedNotes sn where sn.note.noteid = :noteid")
    List<User> getUsersFromSharedNote(@Param("noteid") Long noteid);

    @Query(
            value = "SELECT * FROM note INNER JOIN user ON user.userid = note.user_id WHERE note.user_id = :userid OR note.noteid IN (SELECT sharedNote.note_noteid FROM sharedNote INNER JOIN note ON sharedNote.note_noteid = note.noteid WHERE sharedNote.user_userid = :userid) ORDER BY note.noteid DESC",
            nativeQuery = true)
    List<Note> getAllNotesFromUser(@Param("userid") Long userid, Pageable pageable);


    @Query(
            value = "SELECT * FROM note INNER JOIN user ON user.userid = note.user_id WHERE note.user_id = :userid AND (note.title LIKE %:search% OR note.body LIKE %:search%) AND creationDate BETWEEN :initDate AND :endDate OR note.noteid IN (SELECT sharedNote.note_noteid FROM sharedNote INNER JOIN note ON sharedNote.note_noteid = note.noteid WHERE sharedNote.user_userid = :userid AND (note.title LIKE %:search% OR note.body LIKE %:search%) AND creationDate BETWEEN :initDate AND :endDate) ORDER BY note.noteid DESC",
            nativeQuery = true)
    List<Note> filterNotesByAll(@Param("userid") Long userid, @Param("search") String search, @Param("initDate") String initDate, @Param("endDate") String endDate, Pageable pageable);

    //Filters title, creation date, last update

    @Query(
            value = "SELECT * FROM note INNER JOIN user ON user.userid = note.user_id WHERE note.user_id = :userid AND (note.title LIKE %:search% OR note.body LIKE %:search%) AND creationDate BETWEEN :initDate AND :endDate OR note.noteid IN (SELECT sharedNote.note_noteid FROM sharedNote INNER JOIN note ON sharedNote.note_noteid = note.noteid WHERE sharedNote.user_userid = :userid AND (note.title LIKE %:search% OR note.body LIKE %:search%) AND creationDate BETWEEN :initDate AND :endDate) ORDER BY note.title DESC",
            nativeQuery = true)
    List<Note> filterOrderByTitleDESC(@Param("userid") Long userid, @Param("search") String search, @Param("initDate") String initDate, @Param("endDate") String endDate, Pageable pageable);

    @Query(
            value = "SELECT * FROM note INNER JOIN user ON user.userid = note.user_id WHERE note.user_id = :userid AND (note.title LIKE %:search% OR note.body LIKE %:search%) AND creationDate BETWEEN :initDate AND :endDate OR note.noteid IN (SELECT sharedNote.note_noteid FROM sharedNote INNER JOIN note ON sharedNote.note_noteid = note.noteid WHERE sharedNote.user_userid = :userid AND (note.title LIKE %:search% OR note.body LIKE %:search%) AND creationDate BETWEEN :initDate AND :endDate) ORDER BY note.title ASC",
            nativeQuery = true)
    List<Note> filterOrderByTitleASC(@Param("userid") Long userid, @Param("search") String search, @Param("initDate") String initDate, @Param("endDate") String endDate, Pageable pageable);

    @Query(
            value = "SELECT * FROM note INNER JOIN user ON user.userid = note.user_id WHERE note.user_id = :userid AND (note.title LIKE %:search% OR note.body LIKE %:search%) AND creationDate BETWEEN :initDate AND :endDate OR note.noteid IN (SELECT sharedNote.note_noteid FROM sharedNote INNER JOIN note ON sharedNote.note_noteid = note.noteid WHERE sharedNote.user_userid = :userid AND (note.title LIKE %:search% OR note.body LIKE %:search%) AND creationDate BETWEEN :initDate AND :endDate) ORDER BY note.creationDate DESC",
            nativeQuery = true)
    List<Note> filterOrderByCreationDateDESC(@Param("userid") Long userid, @Param("search") String search, @Param("initDate") String initDate, @Param("endDate") String endDate, Pageable pageable);

    @Query(
            value = "SELECT * FROM note INNER JOIN user ON user.userid = note.user_id WHERE note.user_id = :userid AND (note.title LIKE %:search% OR note.body LIKE %:search%) AND creationDate BETWEEN :initDate AND :endDate OR note.noteid IN (SELECT sharedNote.note_noteid FROM sharedNote INNER JOIN note ON sharedNote.note_noteid = note.noteid WHERE sharedNote.user_userid = :userid AND (note.title LIKE %:search% OR note.body LIKE %:search%) AND creationDate BETWEEN :initDate AND :endDate) ORDER BY note.creationDate ASC",
            nativeQuery = true)
    List<Note> filterOrderByCreationDateASC(@Param("userid") Long userid, @Param("search") String search, @Param("initDate") String initDate, @Param("endDate") String endDate, Pageable pageable);

    @Query(
            value = "SELECT * FROM note INNER JOIN user ON user.userid = note.user_id WHERE note.user_id = :userid AND (note.title LIKE %:search% OR note.body LIKE %:search%) AND creationDate BETWEEN :initDate AND :endDate OR note.noteid IN (SELECT sharedNote.note_noteid FROM sharedNote INNER JOIN note ON sharedNote.note_noteid = note.noteid WHERE sharedNote.user_userid = :userid AND (note.title LIKE %:search% OR note.body LIKE %:search%) AND creationDate BETWEEN :initDate AND :endDate) ORDER BY note.lastModification DESC",
            nativeQuery = true)
    List<Note> filterOrderByLastModificationDESC(@Param("userid") Long userid, @Param("search") String search, @Param("initDate") String initDate, @Param("endDate") String endDate, Pageable pageable);

    @Query(
            value = "SELECT * FROM note INNER JOIN user ON user.userid = note.user_id WHERE note.user_id = :userid AND (note.title LIKE %:search% OR note.body LIKE %:search%) AND creationDate BETWEEN :initDate AND :endDate OR note.noteid IN (SELECT sharedNote.note_noteid FROM sharedNote INNER JOIN note ON sharedNote.note_noteid = note.noteid WHERE sharedNote.user_userid = :userid AND (note.title LIKE %:search% OR note.body LIKE %:search%) AND creationDate BETWEEN :initDate AND :endDate) ORDER BY note.lastModification ASC",
            nativeQuery = true)
    List<Note> filterOrderByLastModificationASC(@Param("userid") Long userid, @Param("search") String search, @Param("initDate") String initDate, @Param("endDate") String endDate, Pageable pageable);
}
