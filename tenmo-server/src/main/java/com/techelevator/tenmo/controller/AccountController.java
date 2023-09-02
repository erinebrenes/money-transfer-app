package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {
    UserDao userDao;
    AccountDao accountDao;
    TransferDao transferDao;

    @Autowired
    public AccountController(UserDao userDao, AccountDao accountDao, TransferDao transferDao) {
        this.userDao = userDao;
        this.accountDao = accountDao;
        this.transferDao = transferDao;
    }

    @GetMapping(path = "account/{id}")
    public Account getAccountByAccountId(@PathVariable int id) {
        Account account = accountDao.getAccountByAccountId(id);
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found for id: " + id);
        }
        return account;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "balance/{id}")
    public Double getBalanceByUserId(@PathVariable int id) {
        Double account = accountDao.getBalanceByUserId(id);
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found for id: " + id);
        }
        return account;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping(path = "account/{id}/withdraw")
    public void subtractBalance(@PathVariable int id, @RequestBody Map<String, Double> requestBody) {
        Double amountToSubtract = requestBody.get("amountToSubtract");
        if (amountToSubtract != null && amountToSubtract > 0) {
            Account account = accountDao.findAccountByUserId(id);
            if (account == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found for id: " + id);
            }
            if (account.getBalance().compareTo(amountToSubtract) <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds.");
            }

            accountDao.subtractBalance(id, amountToSubtract);
        } else {
            throw new IllegalArgumentException("Invalid withdrawal amount.");
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping(path = "account/{id}/deposit")
    public void addBalance(@PathVariable int id, @RequestBody Map<String, Double> requestBody) {
        Double amountToAdd = requestBody.get("amountToAdd");
        if (amountToAdd != null && amountToAdd > 0) {
            Account account = accountDao.getAccountByUserId(id);
            if (account == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found for id: " + id);
            }

            accountDao.addBalance(id, amountToAdd);
        } else {
            throw new IllegalArgumentException("Invalid amount to add.");
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping(path = "account/{id}/update-balance")
    public void updateBalance(@PathVariable int id, @RequestBody Double newBalance){
        accountDao.updateBalance(id, newBalance);
    }

}