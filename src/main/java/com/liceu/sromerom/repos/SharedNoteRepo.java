package com.liceu.sromerom.repos;



import com.liceu.sromerom.entities.SharedNote;
import com.liceu.sromerom.entities.SharedNoteCK;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SharedNoteRepo extends JpaRepository<SharedNote, SharedNoteCK> {
    //SharedNotes
    List<SharedNote> findByNote_User_Userid(long userid, Pageable pageable);

    //LengthSharedNotes
    long countByNote_User_Userid(long userid);

    //SharedNotesWithMe
    List<SharedNote> findByUser_Userid(long userid, Pageable pageable);

    //LengthSharedNoteWithMe
    long countByUser_Userid(long userid);

    //Exists sharedNote?
    SharedNote findByUser_UseridAndNote_Noteid(long userid, long noteid);


    List<SharedNote> findByNote_Noteid(long noteid);

    void deleteSharedNotesByNote_Noteid(long noteid);

}
