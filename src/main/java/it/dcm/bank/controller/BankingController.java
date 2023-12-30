package it.dcm.bank.controller;

import it.dcm.bank.generated.api.BankingApi;
import it.dcm.bank.generated.dto.BalanceResponseDto;
import it.dcm.bank.generated.dto.TransactionsResponseDto;
import it.dcm.bank.generated.dto.TransferRequestDto;
import it.dcm.bank.generated.dto.TransferResponseDto;
import it.dcm.bank.service.BankingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;


@Slf4j
@RestController
@AllArgsConstructor
public class BankingController implements BankingApi {
    private final BankingService bankingService;

    @Override
    public ResponseEntity<BalanceResponseDto> accountBalance() {
        return ResponseEntity.ok(bankingService.getBalance());
    }

    @Override
    public ResponseEntity<TransactionsResponseDto> accountTransactions(LocalDate fromDate, LocalDate toDate) {
        return ResponseEntity.ok(bankingService.getTransactions(fromDate, toDate));
    }

    @Override
    public ResponseEntity<TransferResponseDto> moneyTransfer(TransferRequestDto transferRequestDto) {
        return ResponseEntity.ok(bankingService.createMoneyTransefer(transferRequestDto));
    }

}
