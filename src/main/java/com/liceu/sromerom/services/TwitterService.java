package com.liceu.sromerom.services;

import java.net.URL;
import java.util.Map;

public interface TwitterService {

    Map<String, String> getRequestToken();
    Map<String, String> getAccessToken(String oauth_token, String oauth_verifier);
    Map<String, Object> getAccountDetails(String oauth_token, String oauth_token_secret);
    URL getUrlRedirectTwitter(String requestToken);
}
