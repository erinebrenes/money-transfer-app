package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {
    List<Transfer> listAllTransfers(int accountId);
    List<Transfer> listTransferByUserId(Integer accountId);
    Transfer getTransferByUserId(Integer transactionId);
    Transfer getTransferByTransferId(Integer transactionId);
    Transfer createTransfer(Transfer transfer);
    void updateTransferStatus(int transactionId, int transactionStatusId);

}
