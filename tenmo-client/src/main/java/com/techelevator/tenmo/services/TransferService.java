package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TransferService {

    private final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;
    public void setAuthToken(String authToken) {
        this.authToken = authToken;

    }

    private <T> HttpEntity<T> makeEntity(T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(body, headers);
    }

    public Transfer[] getTransferByUserId(Integer accountId) {
        Transfer[] userTransfer = null;
        try {
            ResponseEntity<Transfer[]> response = restTemplate.getForEntity(API_BASE_URL + "/user/" + accountId + "/transfer", Transfer[].class);
            userTransfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return userTransfer;

    }

    public Transfer sendTransfer(Transfer transfer, int id, int outId) {
        Transfer userTransfer = null;
        try {
            ResponseEntity<Transfer> response =
                    restTemplate.exchange(API_BASE_URL + id + "/" + outId + "/send",
                            HttpMethod.POST, makeEntity(transfer), Transfer.class);
            userTransfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return userTransfer;

    }

}