package com.liceu.sromerom.services;

import java.net.URL;
import java.util.Map;

public interface FacebookService {
    URL getFacebookURL() throws Exception;
    String getAccessToken(String code) throws Exception;

    Map<String, Object> getData(String accesstoken) throws Exception;
}
