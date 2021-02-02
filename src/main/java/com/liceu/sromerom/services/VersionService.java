package com.liceu.sromerom.services;

import com.liceu.sromerom.entities.Version;

import java.util.List;

public interface VersionService {

    List<Version> getVersionsFromNote(Long noteid);

    Version getVersionById(Long versionid);
}
