package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.util.List;

public interface AccountDao {
    List<Account> findAllAccounts();
    Account findAccountByUserId(int userId);
    Account getBalanceByAccountId(int account_Id);
    Double getBalanceByUserId(int id);
    Account getAccountByAccountId(int account_Id);
    Account getAccountByUserId(int user_Id);
    public Double subtractBalance(int id, Double amountToSubtract);
    public Double addBalance(int id, Double amountToAdd);
    public void updateBalance(int accountId, Double newBalance);

}