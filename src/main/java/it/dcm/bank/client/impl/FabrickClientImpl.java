package it.dcm.bank.client.impl;


import it.dcm.bank.client.FabrickClient;
import it.dcm.bank.exception.ResponseClientException;
import it.dcm.bank.generated.client.fabrick.v4.api.BankAccountApi;
import it.dcm.bank.generated.client.fabrick.v4.api.PaymentsMoneyTransferApi;
import it.dcm.bank.generated.client.fabrick.v4.dto.*;
import it.dcm.bank.utility.DateUtility;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.time.LocalDate;

@Slf4j
@Component
@Scope("prototype")
@AllArgsConstructor
public class FabrickClientImpl implements FabrickClient {
    private final BankAccountApi bankAccountApi;
    private final PaymentsMoneyTransferApi paymentsMoneyTransferApi;

    @Override
    public AccountBalanceDto getAccountBalance(String accountId) {
        try {
            log.info("[{}] RETRIEVING BALANCE ACCOUNT", accountId);
            AccountBalanceResponseDto response = this.bankAccountApi.cashAccountBalance(accountId);
            if (response.getStatus() == AccountBalanceResponseDto.StatusEnum.KO) {
                log.error("[{}] PROBLEM WITH RETRIEVING BALANCE ACCOUNT", accountId);
                throw new ResponseClientException(response.getErrors());
            }
            log.info("[{}] RETRIEVED BALANCE ACCOUNT", accountId);
            return response.getPayload();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error("[{}] PROBLEM WITH RETRIEVING BALANCE ACCOUNT", accountId, ex);
            throw ex;
        }
    }

    @Override
    public AccountTransactionsDto getAllTransactions(String accountId, LocalDate from, LocalDate to) {
        try {
            log.info("[{}] RETRIEVING ALL ACCOUNT TRANSACTIONS FROM {} TO {}", accountId, DateUtility.getDateString(from), DateUtility.getDateString(to));
            AccountTransactionsResponseDto response = this.bankAccountApi.cashAccountTransactions(accountId, from, to);
            if (response.getStatus() == AccountTransactionsResponseDto.StatusEnum.KO) {
                log.error("[{}] PROBLEM WITH RETRIEVING ALL ACCOUNT TRANSACTIONS", accountId);
                throw new ResponseClientException(response.getErrors());
            }
            log.info("[{}] RETRIEVED ALL ACCOUNT TRANSACTIONS", accountId);
            return response.getPayload();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error("[{}] PROBLEM WITH RETRIEVING ALL ACCOUNT TRANSACTIONS", accountId, ex);
            throw ex;
        }
    }

    @Override
    public MoneyTransferDto createMoneyTransfer(String accountId, MoneyTransferRequestDto request) {
        try {
            log.info("[{}] CREATING MONEY TRANSFER", accountId);
            MoneyTransferResponseDto moneyTransferResponseDto = this.paymentsMoneyTransferApi.createMoneyTransfer(accountId, request);
            if (moneyTransferResponseDto.getStatus() == MoneyTransferResponseDto.StatusEnum.KO || moneyTransferResponseDto.getPayload() == null) {
                log.error("[{}] PROBLEM WITH CREATING MONEY TRANSFER", accountId);
                throw new ResponseClientException(moneyTransferResponseDto.getErrors());
            }
            MoneyTransferDto moneyTransferDto = moneyTransferResponseDto.getPayload();
            log.info("[{}] CREATED MONEY TRANSFER WITH ID {} AND STATUS {}", accountId, moneyTransferDto.getMoneyTransferId(), moneyTransferDto.getStatus().getValue());
            return moneyTransferResponseDto.getPayload();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error("[{}] PROBLEM WITH CREATING MONEY TRANSFER", accountId, ex);
            throw ex;
        }
    }

}
