package com.liceu.sromerom.repos;

import com.liceu.sromerom.entities.Version;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VersionRepo extends JpaRepository<Version, Long> {
    List<Version> findByNote_NoteidOrderByVersionidDesc(Long noteid);
}
