package com.liceu.sromerom.services;

import com.liceu.sromerom.entities.Version;
import com.liceu.sromerom.repos.VersionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VersionServiceImpl implements VersionService {

    @Autowired
    VersionRepo versionRepo;

    @Override
    public List<Version> getVersionsFromNote(Long noteid) {
        return versionRepo.findByNote_NoteidOrderByVersionidDesc(noteid);
    }

    @Override
    public Version getVersionById(Long versionid) {
        return versionRepo.findById(versionid).get();
    }
}
