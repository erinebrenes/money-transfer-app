package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.jboss.logging.BasicLogger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    private TransferDao transferDao;
    private AccountDao accountDao;
    private Account account = new Account();
    public TransferController(TransferDao transferDao, AccountDao accountDao) {
        this.transferDao= transferDao;
        this.accountDao = accountDao;
    }

    @PostMapping("/transfers")
    public ResponseEntity<Transfer> createTransfer(@RequestBody Transfer transfer) {
        Transfer savedTransfer = transferDao.createTransfer(transfer);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTransfer);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/{accountId}/transfer")
    public List<Transfer> listAllTransfers(@PathVariable("accountId") Integer accountId) {
        return transferDao.listAllTransfers(accountId);

    }

    @PutMapping("/transfers/{transferId}/status")
    public ResponseEntity<String> updateTransferStatus(@PathVariable int transferId, @RequestBody int transferStatusId) {
        // Call the transfer service to update the transfer status
        transferDao.updateTransferStatus(transferId, transferStatusId);
        // Return a response with a success message
        return ResponseEntity.ok("Transfer status updated successfully");
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/{id}/{outId}/send")
    public Transfer sendTransfer(@PathVariable int id, @PathVariable int outId, @RequestBody Transfer transfer) {
        int currentUser = id;
        int accountTo = outId;
        double amount = transfer.getAmount();

        accountDao.subtractBalance(currentUser, amount);

        accountDao.addBalance(accountTo, amount);

        transferDao.createTransfer(transfer);

        return transfer;
    }

}