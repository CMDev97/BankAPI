package it.dcm.bank.client;

import it.dcm.bank.generated.client.fabrick.v4.dto.AccountBalanceDto;
import it.dcm.bank.generated.client.fabrick.v4.dto.AccountTransactionsDto;
import it.dcm.bank.generated.client.fabrick.v4.dto.MoneyTransferDto;
import it.dcm.bank.generated.client.fabrick.v4.dto.MoneyTransferRequestDto;

import java.time.LocalDate;

public interface FabrickClient {

    AccountBalanceDto getAccountBalance(String accountId);
    AccountTransactionsDto getAllTransactions(String accountId, LocalDate from, LocalDate to);
    MoneyTransferDto createMoneyTransfer(String accountId, MoneyTransferRequestDto request);

}
