package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    UserDao userDao;

    @Autowired
    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

   @PreAuthorize("permitAll")
   @GetMapping(path = "user/list")
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        List<User> findUsers = userDao.findAllUsers();

        for (int i = 0; i < findUsers.size(); i++) {
            userList.add(findUsers.get(i));
        }

        return userList;
    }

    @PreAuthorize("permitAll")
    @GetMapping(path = "user/{id}")
    public User selectUserById(@PathVariable int id) {
        User user = userDao.getUserById(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found for id: " + id);
        } else {
            return user;
        }
    }

}