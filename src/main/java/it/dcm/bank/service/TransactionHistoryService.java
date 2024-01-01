package it.dcm.bank.service;

import it.dcm.bank.generated.dto.TransactionDto;

import java.util.List;

public interface TransactionHistoryService {

    void saveAllTransactions(List<TransactionDto> transactions);

}
