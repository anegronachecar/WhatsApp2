package com.swe.whatscooking.service;

import com.swe.whatscooking.entity.KrogerClient;
import com.swe.whatscooking.entity.KrogerCustomer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
public class KrogerService {
    private RestTemplate restTemplate;

    public KrogerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public KrogerCustomer getAccessToken(String code){
        // API Website link https://sv443.net/jokeapi/v2/
        String clientId = "whatscooking-acf72f4b2dbda6aa726382a398050a863025969198037206606";
        String clientSecret = "5yRrceH3sR53QNA7AfC9JTp6I4KTYdmVCvDyBxxA";
        String body = "grant_type=authorization_code";
        String redirectUri = "http://127.0.0.1:8080/callback";
        body = body + "&code=" + code;
        body = body + "&redirect_uri=" + redirectUri;
        byte[] encodedBytes = Base64.getEncoder().encode((clientId + ":" + clientSecret).getBytes());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        responseHeaders.add("Authorization", "Basic " + new String(encodedBytes));
//		System.out.println(responseHeaders.toString());
        HttpEntity<String> request = new HttpEntity<>(body, responseHeaders);

//		System.out.println("Has Body? " + request.hasBody());
//		System.out.println("Here is the body Body? " + request.getBody());
        KrogerCustomer krogerCustomer = restTemplate.postForObject("https://api.kroger.com/v1/connect/oauth2/token",request, KrogerCustomer.class);
        //log.info(krogerClient.toString());

        return krogerCustomer;
    }

    public void orderProducts(String token, String products){
        // API Website link https://sv443.net/jokeapi/v2/
        //String clientId = "whatscooking-acf72f4b2dbda6aa726382a398050a863025969198037206606";
        //String clientSecret = "5yRrceH3sR53QNA7AfC9JTp6I4KTYdmVCvDyBxxA";
        String body = "{ \"items\": [{\"upc\": \"0001111041700\",\"quantity\": 1}]}";
        //byte[] encodedBytes = Base64.getEncoder().encode((clientId + ":" + clientSecret).getBytes());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        responseHeaders.add("Authorization", "Bearer " + token);
//		System.out.println(responseHeaders.toString());
        HttpEntity<String> request = new HttpEntity<>(body, responseHeaders);
        System.out.println(token);
		System.out.println("Has Body? " + request.hasBody());
		System.out.println("Here is the body Body? " + request.getBody());
        restTemplate.put("https://api.kroger.com/v1/cart/add",request);
        //KrogerClient krogerClient = restTemplate.postForObject("https://api.kroger.com/v1/cart/add",request, KrogerClient.class);
        //log.info(krogerClient.toString());
        return;

    }

}
