package com.liceu.sromerom.services;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.client.utils.URIBuilder;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Service
public class GoogleServiceImpl implements GoogleService{
    @Value("${client-id}")
    String clientId;

    @Value("${client-secret}")
    String clientSecret;

    @Value("${redirect-uri}")
    String redirectUri;

    public Map<String, String> getUserDetails(String accessToken) throws Exception{
        URIBuilder b = new URIBuilder("https://www.googleapis.com/oauth2/v1/userinfo");
        b.addParameter("access_token", accessToken);
        b.addParameter("alt", "json");

        URL url = b.build().toURL();
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        String content = req(con);

        return new Gson().fromJson(content, HashMap.class);

    }

    public String getAccessToken(String code) throws Exception {
        URL url = new URL("https://oauth2.googleapis.com/token");
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        String query = "client_id=" + URLEncoder.encode(clientId) + "&";
        query += "client_secret=" + URLEncoder.encode(clientSecret) + "&";
        query += "code=" + URLEncoder.encode(code) + "&";
        query += "grant_type" + URLEncoder.encode("authorization_code") + "&";
        query += "redirect_uri" + URLEncoder.encode(redirectUri);

        con.setRequestProperty("Content-length", String.valueOf(query.length()));
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        con.setDoInput(true);
        con.setDoOutput(true);
        DataOutputStream output = new DataOutputStream(con.getOutputStream());
        output.writeBytes(query);
        output.close();

        String content = req(con);
        JSONParser jp = new JSONParser();
        JsonElement root = (JsonElement) jp.parse(content);
        JsonObject rootObject = root.getAsJsonObject();
        System.out.println(rootObject.toString());
        return rootObject.toString();
    }

    public String req(HttpsURLConnection con) throws Exception{
        int status = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        boolean inputline;
        StringBuffer content = new StringBuffer();
        while (inputline = in.readLine() != null) {
            content.append(inputline);
        }

        in.close();
        return content.toString();
    }


    public URL getGoogleRedirectURL()throws Exception{
        System.out.println("clientID: " + clientId);
        System.out.println("client secret: " + clientSecret);
        System.out.println("redirect uri: " + redirectUri);
        URIBuilder b = new URIBuilder("https://accounts.google.com/o/oauth2/v2/auth");
        b.addParameter("scope", "https://www.googleapis.com/auth/userinfo.email");
        b.addParameter("access_type", "offline");
        b.addParameter("state", "state_parameter_passthrough_value");
        b.addParameter("client_id", clientId);
        b.addParameter("redirect_uri", redirectUri);
        b.addParameter("response_type", "code");
        return b.build().toURL();
    }
}
