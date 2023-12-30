package it.dcm.bank.service;


import it.dcm.bank.generated.dto.BalanceResponseDto;
import it.dcm.bank.generated.dto.TransactionsResponseDto;
import it.dcm.bank.generated.dto.TransferRequestDto;
import it.dcm.bank.generated.dto.TransferResponseDto;

import java.time.LocalDate;

public interface BankingService {

    BalanceResponseDto getBalance();
    TransactionsResponseDto getTransactions(LocalDate from,LocalDate to);
    TransferResponseDto createMoneyTransefer(TransferRequestDto transferRequest);

}
