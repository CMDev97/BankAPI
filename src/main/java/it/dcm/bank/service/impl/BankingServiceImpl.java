package it.dcm.bank.service.impl;

import it.dcm.bank.client.FabrickClient;
import it.dcm.bank.exception.ValidationException;
import it.dcm.bank.generated.client.fabrick.v4.dto.AccountBalanceDto;
import it.dcm.bank.generated.client.fabrick.v4.dto.AccountTransactionsDto;
import it.dcm.bank.generated.client.fabrick.v4.dto.MoneyTransferDto;
import it.dcm.bank.generated.client.fabrick.v4.dto.MoneyTransferRequestDto;
import it.dcm.bank.generated.dto.*;
import it.dcm.bank.mapper.BalanceMapper;
import it.dcm.bank.mapper.TransactionMapper;
import it.dcm.bank.mapper.TransferMapper;
import it.dcm.bank.service.BankingService;
import it.dcm.bank.utility.DateUtility;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static it.dcm.bank.exception.ExceptionTypeEnum.*;


@Slf4j
@Service
@AllArgsConstructor
public class BankingServiceImpl implements BankingService {
    private static final String ACCOUNT_ID = "14537780";
    private final FabrickClient fabrickClient;
    private final BalanceMapper balanceMapper;
    private final TransactionMapper transactionMapper;
    private final TransferMapper transferMapper;


    @Override
    public BalanceResponseDto getBalance() {
        AccountBalanceDto accountBalance = this.fabrickClient.getAccountBalance(ACCOUNT_ID);
        return balanceMapper.mapFromClient(accountBalance);
    }

    @Override
    public TransactionsResponseDto getTransactions(LocalDate from, LocalDate to) {
        if (!DateUtility.validRange(from, to))
            throw new ValidationException(RANGE_INTERVAL_NOT_VALID.getCode(), RANGE_INTERVAL_NOT_VALID.getMessage());

        AccountTransactionsDto responseClient = this.fabrickClient.getAllTransactions(ACCOUNT_ID, from, to);
        TransactionsResponseDto transactionsResponseDto = new TransactionsResponseDto();

        if (responseClient == null || responseClient.getList() == null) return transactionsResponseDto;

        List<TransactionDto> transactions = responseClient.getList()
                .parallelStream()
                .map(transactionMapper::mapFromClient)
                .toList();

        transactionsResponseDto.setList(transactions);
        return transactionsResponseDto;
    }

    @Override
    public TransferResponseDto createMoneyTransefer(TransferRequestDto transferRequest) {

        executionDateSetting(transferRequest);

        MoneyTransferRequestDto moneyTransferRequest = transferMapper.toClientRequest(transferRequest);

        MoneyTransferDto transferResponse = this.fabrickClient.createMoneyTransfer(ACCOUNT_ID, moneyTransferRequest);
        return transferMapper.mapClientResponse(transferResponse);
    }

    private void executionDateSetting(TransferRequestDto transferRequest) {
        if (transferRequest.getIsInstant()) transferRequest.setExecutionDate(LocalDate.now());

        if (transferRequest.getExecutionDate() == null)
            throw new ValidationException(INPUT_MISSING_DATA.getCode(), INPUT_MISSING_DATA.getMessage().concat(" execution date is null"));

        if (transferRequest.getExecutionDate().isBefore(LocalDate.now()))
            throw new ValidationException(INVALID_DATE.getCode(), INVALID_DATE.getMessage());

    }
}
