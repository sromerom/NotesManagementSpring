package com.liceu.sromerom.services;

import com.google.gson.Gson;
import com.liceu.sromerom.utils.HmacSha1Signature;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.*;

@Service
public class TwitterServiceImpl implements TwitterService {

    @Value("${twitter-apikey}")
    String twitterApikey;

    @Value("${twitter-secret-apikey}")
    String twitterSecretApikey;

    @Value("${twitter-access-token-secret}")
    String twitterAccessTokenSecret;

    @Value("${twitter-access-token}")
    String twitterAccessToken;

    @Value("${twitter-bearer-token}")
    String twitterBearerToken;

    @Value("${twitter-redirect-uri}")
    String twitterRedirectUri;


    @Override
    public Map<String, String> getRequestToken() {
        try {
            String actualTimestamp = Long.toString(System.currentTimeMillis() / 1000L);
            URL url = new URL("https://api.twitter.com/oauth/request_token");
            Map<String, String> requestParameters = new LinkedHashMap<>();
            String nonce = generateNonce();

            requestParameters.put("oauth_callback", URLEncoder.encode(twitterRedirectUri, "UTF-8"));
            requestParameters.put("oauth_consumer_key", twitterApikey);
            requestParameters.put("oauth_nonce", nonce);
            requestParameters.put("oauth_signature", "");
            requestParameters.put("oauth_signature_method", "HMAC-SHA1");
            requestParameters.put("oauth_timestamp", actualTimestamp);
            requestParameters.put("oauth_version", "1.0");


            Map<String, String> requestParameterCopy = new LinkedHashMap<>(requestParameters);
            requestParameters.replace("oauth_signature", URLEncoder.encode(buildSignature(url, requestParameterCopy,"POST", ""), "UTF-8"));


            String content = doPost(url, requestParameters, null);
            Map<String, String> responseRequestToken = parseStringToMap(content);
           return responseRequestToken;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, String> getAccessToken(String oauth_token, String oauth_verifier) {
        try {
            String actualTimestamp = Long.toString(System.currentTimeMillis() / 1000L);
            URL url = new URL("https://api.twitter.com/oauth/access_token");
            Map<String, String> authorizationHeaderParams = new LinkedHashMap<>();
            String nonce = generateNonce();

            authorizationHeaderParams.put("oauth_consumer_key", twitterApikey);
            authorizationHeaderParams.put("oauth_nonce", nonce);
            authorizationHeaderParams.put("oauth_signature", "");
            authorizationHeaderParams.put("oauth_signature_method", "HMAC-SHA1");
            authorizationHeaderParams.put("oauth_timestamp", actualTimestamp);
            authorizationHeaderParams.put("oauth_token", oauth_token);
            authorizationHeaderParams.put("oauth_version", "1.0");

            Map<String, String> authorizationHeaderParamsCopy = new LinkedHashMap<>(authorizationHeaderParams);
            authorizationHeaderParams.replace("oauth_signature", URLEncoder.encode(buildSignature(url, authorizationHeaderParamsCopy,"POST", ""), "UTF-8"));

            Map<String, String> bodyParams = new LinkedHashMap<>();
            bodyParams.put("oauth_verifier", oauth_verifier);

            String content = doPost(url, authorizationHeaderParams, bodyParams);
            Map<String, String> responseRequestToken = parseStringToMap(content);
            return responseRequestToken;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, Object> getAccountDetails(String oauth_token, String oauth_token_secret) {
        try {
            URL url = new URL("https://api.twitter.com/1.1/account/verify_credentials.json");
            URL urlWithParams = new URL("https://api.twitter.com/1.1/account/verify_credentials.json?include_email=true");
            String actualTimestamp = Long.toString(System.currentTimeMillis() / 1000L);
            Map<String, String> authorizationHeaderParams = new LinkedHashMap<>();
            String nonce = generateNonce();

            //include_entities
            //include_email
            //authorizationHeaderParams.put("include_entities", "false");
            authorizationHeaderParams.put("include_email", "true");
            authorizationHeaderParams.put("oauth_consumer_key", twitterApikey);
            authorizationHeaderParams.put("oauth_nonce", nonce);
            authorizationHeaderParams.put("oauth_signature", "");
            authorizationHeaderParams.put("oauth_signature_method", "HMAC-SHA1");
            authorizationHeaderParams.put("oauth_timestamp", actualTimestamp);
            authorizationHeaderParams.put("oauth_token", oauth_token);
            authorizationHeaderParams.put("oauth_version", "1.0");

            Map<String, String> authorizationHeaderParamsCopy = new LinkedHashMap<>(authorizationHeaderParams);
            authorizationHeaderParams.replace("oauth_signature", URLEncoder.encode(buildSignature(url, authorizationHeaderParamsCopy, "GET", oauth_token_secret), "UTF-8"));

            String content = doGet(urlWithParams, authorizationHeaderParams);
            return new Gson().fromJson(content, HashMap.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public URL getUrlRedirectTwitter(String oauth_token) {
        try {
            URIBuilder b = new URIBuilder("https://api.twitter.com/oauth/authorize");
            b.addParameter("oauth_token", oauth_token);
            return b.build().toURL();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private String buildSignature(URL url, Map<String, String> parameters, String method, String secretToken) throws MalformedURLException {
        try {

            parameters.replace("oauth_callback", twitterRedirectUri);
            String returned_value = (String)parameters.remove("oauth_signature");

            String encode = encode(parameters);
            String baseString = generateBaseString(url, method, encode);


            String signingKey = URLEncoder.encode(twitterSecretApikey, "UTF-8") + "&" + URLEncoder.encode(secretToken, "UTF-8");
            String hmac = HmacSha1Signature.computeSignature(baseString, signingKey);
            return hmac;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String generateBaseString(URL url, String method, String encode) throws UnsupportedEncodingException {
        String result = "";
        result+=method.toUpperCase() + "&";
        result+=URLEncoder.encode(url.toString(), "UTF-8");
        result+="&";
        result+= URLEncoder.encode(encode, "UTF-8");
        return result;
    }
    private String encode(Map<String, String> parameters) throws Exception {
        List<NameValuePair> nvps = new ArrayList<>();
        for (String s : parameters.keySet()) {
            String key = URLEncoder.encode(s, "UTF-8");
            String value = URLEncoder.encode(parameters.get(s), "UTF-8");
            nvps.add(new BasicNameValuePair(key, value));
        }

        StringBuilder encodedParameters = new StringBuilder();
        int aux = 0;
        for (NameValuePair nvp : nvps) {
            if (aux == nvps.size() - 1) {
                encodedParameters.append(nvp);
            } else {
                encodedParameters.append(nvp + "&");
            }
            aux++;
        }

        return encodedParameters.toString();
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

    private String doPost(URL url, Map<String, String> headerParameters, Map<String, String> bodyParameters) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url.toString());
        StringBuilder authorization = new StringBuilder();
        if (headerParameters != null) {
            List<NameValuePair> nvps = new ArrayList<>();
            for (String s : headerParameters.keySet()) {
                nvps.add(new BasicNameValuePair(s, headerParameters.get(s)));
            }
            int aux = 0;
            for (NameValuePair nvp : nvps) {
                if (aux == nvps.size() - 1) {
                    authorization.append(nvp.getName() + '=' + '"' + nvp.getValue() + '"');
                } else {
                    authorization.append(nvp.getName() + '=' + '"' + nvp.getValue() + '"' + ",");
                }
                aux++;
            }
        }


        if (bodyParameters != null) {
            List<NameValuePair> nvps = new ArrayList<>();
            for(String s: bodyParameters.keySet()) {
                nvps.add(new BasicNameValuePair(s, bodyParameters.get(s)));
            }
            post.setEntity(new UrlEncodedFormEntity(nvps));
        }

        post.setHeader("Authorization", "OAuth " + authorization);
        CloseableHttpResponse response = httpClient.execute(post);
        response.getEntity();
        return EntityUtils.toString(response.getEntity());
    }

    private String doGet(URL url, Map<String, String> authorizationHeaderParams) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(url.toString());
        StringBuilder authorization = new StringBuilder();
        if (authorizationHeaderParams != null) {
            List<NameValuePair> nvps = new ArrayList<>();
            for (String s : authorizationHeaderParams.keySet()) {
                nvps.add(new BasicNameValuePair(s, authorizationHeaderParams.get(s)));
            }
            int aux = 0;
            for (NameValuePair nvp : nvps) {
                if (aux == nvps.size() - 1) {
                    authorization.append(nvp.getName() + '=' + '"' + nvp.getValue() + '"');
                } else {
                    authorization.append(nvp.getName() + '=' + '"' + nvp.getValue() + '"' + ",");
                }
                aux++;
            }

            get.setHeader("Authorization", "OAuth " + authorization);
        }


        CloseableHttpResponse response = httpClient.execute(get);
        response.getEntity();
        return EntityUtils.toString(response.getEntity());
    }


    private Map<String, String> parseStringToMap(String content) {
        Map<String, String> result = new LinkedHashMap<>();
        String [] parts = content.split("&");
        for (String param: parts) {
            String key = param.split("=")[0];
            String value = param.split("=")[1];
            result.put(key, value);
        }

        return result;
    }
}
