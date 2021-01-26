package com.liceu.sromerom.repos;

import com.liceu.sromerom.entities.Note;
import com.liceu.sromerom.entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoteRepo extends JpaRepository<Note, Long> {
    @Query(
            value = "SELECT * FROM note INNER JOIN user ON user.userid = note.user_id WHERE note.user_id = :userid OR note.noteid IN (SELECT sharedNote.note_noteid FROM sharedNote INNER JOIN note ON sharedNote.note_noteid = note.noteid WHERE sharedNote.user_userid = :userid) ORDER BY note.noteid",
            nativeQuery = true)
    List<Note> getAllNotesFromUser(@Param("userid") Long userid, Pageable pageable);

    //CreatedNotes
    List<Note> findByUser_Userid(long userid, Pageable pageable);

    //LengthCreatedNotes
    long countByUser_Userid(long userid);

    //Is owner note?
    Note findNoteByNoteidAndUser(long noteid, User user);

    @Query("from user u inner join u.sharedNotes sn where sn.note.noteid = :noteid")
    List<User> getUsersFromSharedNote(@Param("noteid") Long noteid);


}
