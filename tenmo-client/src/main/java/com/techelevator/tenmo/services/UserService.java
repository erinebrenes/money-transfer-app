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

public class UserService {
    private final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;
    public void setAuthToken(String authToken) {
        this.authToken = authToken;

    }

    public List<User> getListUsers(){

        List<User>userList = new ArrayList<>();
        try {
            ResponseEntity<User[]> response = restTemplate.exchange(API_BASE_URL + "user/list",
                    HttpMethod.GET, makeEntity(), User[].class);
            userList = Arrays.asList(Objects.requireNonNull(response.getBody()));
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return userList;
    }

    public User selectUserById(int userId) {
        try {
            ResponseEntity<User> response = restTemplate.exchange(API_BASE_URL + "user/" + userId,
                    HttpMethod.GET, makeEntity(), User.class);
            return response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            return null;
        }
    }


    private HttpEntity makeEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<String>(headers);
    }
}