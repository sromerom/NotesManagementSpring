package com.liceu.sromerom.services;

import com.google.gson.Gson;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URL;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@Service
public class FacebookServiceImpl implements FacebookService{

    @Value("${facebook-client-id}")
    String clientId;

    @Value("${facebook-secret-apikey}")
    String clientSecret;

    @Value("${facebook-redirect-uri}")
    String redirectUri;


    @Override
    public URL getFacebookURL() throws Exception {
        URIBuilder b = new URIBuilder("https://www.facebook.com/v9.0/dialog/oauth");
        b.addParameter("client_id", clientId);
        b.addParameter("redirect_uri", redirectUri);
        b.addParameter("state", generateNonce());
        b.addParameter("scope", "email");
        return b.build().toURL();
    }

    @Override
    public String getAccessToken(String code) throws Exception {
        URIBuilder b = new URIBuilder("https://graph.facebook.com/v9.0/oauth/access_token");
        b.addParameter("client_id", clientId);
        b.addParameter("redirect_uri", redirectUri);
        b.addParameter("client_secret", clientSecret);
        b.addParameter("code", code);
        String content = doGet(b.build().toURL());
        Map<String, Object> map = new Gson().fromJson(content, HashMap.class);
        return map.get("access_token").toString();
    }

    @Override
    public Map<String, Object> getData(String accesstoken) throws Exception{
        URIBuilder urlME = new URIBuilder("https://graph.facebook.com/me");
        urlME.addParameter("access_token", accesstoken);
        String basicinfo = doGet(urlME.build().toURL());
        Map<String, Object> basicinfomap = new Gson().fromJson(basicinfo, HashMap.class);
        String facebookuserid = basicinfomap.get("id").toString();

        URIBuilder urlFullData = new URIBuilder("https://graph.facebook.com/" + facebookuserid);
        urlFullData.addParameter("fields", "name,email");
        urlFullData.addParameter("access_token", accesstoken);
        String fullDataInfo = doGet(urlFullData.build().toURL());
        Map<String, Object> fullDataInfoMap = new Gson().fromJson(fullDataInfo, HashMap.class);
        return fullDataInfoMap;
    }

    private String doGet(URL url) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(url.toString());
        CloseableHttpResponse response = httpClient.execute(get);
        response.getEntity();
        return EntityUtils.toString(response.getEntity());
    }

    private String generateNonce(){
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            stringBuilder.append(secureRandom.nextInt(10));
        }
        String randomNumber = stringBuilder.toString();
        return randomNumber;
    }
}
