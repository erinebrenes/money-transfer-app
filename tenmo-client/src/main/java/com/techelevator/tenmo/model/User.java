package com.techelevator.tenmo.model;

import java.util.Objects;

public class User {

    private int id;
    private Double balance;
    private String username;

    public Double getBalance() {
        return balance;
    }
    public User() {
    }

    public User(int id) {
        this.balance = 1000.00;
    }
    public User (int id, Double balance, String username) {
        this.id = id;
        this.balance = balance;
        this.username = username;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof User) {
            User otherUser = (User) other;
            return otherUser.getId() == id
                    && otherUser.getUsername().equals(username);
        } else {
            return false;
        }
    }
    @Override
    public String toString() {

        return "User Details: \n\t" +
                "User id: " + getId() +
                "\n\tUser Balance: " +  getBalance();
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}