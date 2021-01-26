package com.liceu.sromerom.services;

import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.util.Map;

public interface GoogleService {
    Map<String, String> getUserDetails(String accessToken) throws Exception;
    String getAccessToken(String code) throws Exception;

    String req(HttpsURLConnection con) throws Exception;

    URL getGoogleRedirectURL() throws Exception;
}
