package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Account> findAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM account";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            Account account = mapRowToUser(results);
            accounts.add(account);
        }
        return accounts;
    }

    public Account findAccountByUserId(int user_id) {
        String sql = "SELECT account_id, user_id, balance FROM account WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, user_id);
        if (results.next()) {
            Account account = mapRowToUser(results);
            return account;
        }
        return null;
    }

    @Override
    public Account getBalanceByAccountId(int account_Id) {
        String sql = "SELECT balance, account_id " +
                "FROM account WHERE account_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, account_Id);
        if (results.next()) {
            return mapRowToUser(results);
        } else {
            return null;
        }
    }

    @Override
    public Double getBalanceByUserId(int id) {
        String sql = "SELECT balance " +
                "FROM account WHERE user_id = " + id;
        Double results = jdbcTemplate.queryForObject(sql, Double.class);
        return results;
    }

    @Override
    public Account getAccountByAccountId(int account_Id) {
        String sql = "SELECT * " +
                "FROM account WHERE account_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, account_Id);
        if (results.next()) {
            return mapRowToUser(results);
        } else {
            return null;
        }
    }

    @Override
    public Account getAccountByUserId(int user_Id) {
        String sql = "SELECT * " +
                "FROM account WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, user_Id);
        if (results.next()) {
            return mapRowToUser(results);
        } else {
            return null;
        }
    }

    @Override
    public Double subtractBalance(int id, Double amountToSubtract) {
        Account account = findAccountByUserId(id);
        Double currentBalance = account.getBalance();
        Double newBalance = currentBalance - amountToSubtract;
        updateBalance(account.getAccount_id(id), newBalance);
        return newBalance;

    }

    @Override
    public Double addBalance(int id, Double amountToAdd) {
        Account account = findAccountByUserId(id);
        Double currentBalance = account.getBalance();
        Double newBalance = currentBalance + amountToAdd;
        updateBalance(account.getAccount_id(account.getUser_id()), newBalance);
        return newBalance;
    }

    @Override
    public void updateBalance(int accountId, Double newBalance) {
        String sql = "UPDATE account SET balance = ? WHERE account_id = ?";
        jdbcTemplate.update(sql, newBalance, accountId);
    }

    private Account mapRowToUser(SqlRowSet rs) {
        Account account = new Account();
        account.setUser_id(rs.getInt("user_id"));
        account.setAccount_id(rs.getInt("account_id"));
        account.setBalance(rs.getDouble("balance"));
        return account;
    }

}