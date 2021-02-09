package com.liceu.sromerom.repos;


import com.liceu.sromerom.entities.SharedNote;
import com.liceu.sromerom.entities.SharedNoteCK;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SharedNoteRepo extends JpaRepository<SharedNote, SharedNoteCK> {
    //SharedNotes & Filter
    @Query(
            value = "SELECT * FROM sharedNote INNER JOIN note ON sharedNote.note_noteid = note.noteid INNER JOIN user ON user.userid = sharedNote.user_userid WHERE note.user_id = :userid ORDER BY sharedNote.note_noteid DESC",
            nativeQuery = true)
    List<SharedNote> getSharedNotesByUserid(@Param("userid") Long userid, Pageable pageable);

    @Query(
            value = "SELECT DISTINCT * FROM sharedNote INNER JOIN note ON sharedNote.note_noteid = note.noteid INNER JOIN user ON note.user_id = user.userid WHERE note.user_id = :userid AND (note.title REGEXP :search OR note.body REGEXP :search) AND creationDate BETWEEN :initDate AND :endDate ORDER BY note.noteid DESC",
            nativeQuery = true)
    List<SharedNote> filterSharedNotes(@Param("userid") Long userid, @Param("search") String search, @Param("initDate") String initDate, @Param("endDate") String endDate, Pageable pageable);

    //SharedNotesWithMe & Filter
    List<SharedNote> findByUser_Userid(long userid, Pageable pageable);

    @Query(
            value = "SELECT DISTINCT * FROM sharedNote INNER JOIN note ON sharedNote.note_noteid = note.noteid INNER JOIN user ON note.user_id = user.userid WHERE sharedNote.user_userid = :userid AND (note.title REGEXP :search OR note.body REGEXP :search)AND creationDate BETWEEN :initDate AND :endDate ORDER BY note.noteid DESC",
            nativeQuery = true)
    List<SharedNote> filterSharedNotesWithMe(@Param("userid") Long userid, @Param("search") String search, @Param("initDate") String initDate, @Param("endDate") String endDate, Pageable pageable);


    SharedNote findByUser_UseridAndNote_Noteid(long userid, long noteid);
    boolean existsSharedNoteByUser_UseridAndNote_Noteid(long userid, long noteid);

    boolean existsSharedNoteByNote_Noteid(long noteid);


    void deleteSharedNotesByNote_Noteid(long noteid);


}
