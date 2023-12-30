package it.dcm.bank.service.impl;

import it.dcm.bank.client.FabrickClient;
import it.dcm.bank.generated.dto.BalanceResponseDto;
import it.dcm.bank.generated.dto.TransactionsResponseDto;
import it.dcm.bank.generated.dto.TransferRequestDto;
import it.dcm.bank.generated.dto.TransferResponseDto;
import it.dcm.bank.service.BankingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Slf4j
@Service
@AllArgsConstructor
public class BankingServiceImpl implements BankingService {
    private static final String ACCOUNT_ID = "";
    private final FabrickClient fabrickClient;

    @Override
    public BalanceResponseDto getBalance() {
        return null;
    }

    @Override
    public TransactionsResponseDto getTransactions(LocalDate from, LocalDate to) {
        return null;
    }

    @Override
    public TransferResponseDto createMoneyTransefer(TransferRequestDto transferRequest) {
        return null;
    }
}
