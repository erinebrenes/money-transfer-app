package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AccountService {
    private final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;
    public void setAuthToken(String authToken) {
        this.authToken = authToken;

    }

    private HttpEntity makeEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<String>(headers);
    }

    public int getAccountIdByUserId(int id){

        int  accountId = 0;
        try {
            ResponseEntity<Integer> response =
                    restTemplate.exchange(API_BASE_URL + "account/account?id" + id,
                            HttpMethod.GET, makeEntity(), Integer.class);
            accountId = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return accountId;

    }

    public Double getAccountBalance(int userId) {
        Double balance = null;
        try {
            ResponseEntity<Double> response = restTemplate.exchange(API_BASE_URL + "balance/" + userId, HttpMethod.GET, makeEntity(), Double.class);
            balance = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            System.out.println("accepting");
        }
        return balance;
    }
}