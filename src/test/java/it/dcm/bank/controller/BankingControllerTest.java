package it.dcm.bank.controller;

import it.dcm.bank.generated.dto.*;
import it.dcm.bank.service.BankingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class BankingControllerTest {

    @Mock
    private BankingService bankingService;

    @InjectMocks
    private BankingController bankingController;


    @Test
    void accountBalance_ShouldReturnBalance() {
        BalanceResponseDto balanceResponseDto = new BalanceResponseDto();
        when(bankingService.getBalance()).thenReturn(balanceResponseDto);

        ResponseEntity<BalanceResponseDto> response = bankingController.accountBalance();

        assertEquals(ResponseEntity.ok(balanceResponseDto), response);
        verify(bankingService).getBalance();
    }

    @Test
    void accountTransactions_ShouldReturnTransactions() {
        LocalDate fromDate = LocalDate.now();
        LocalDate toDate = LocalDate.now();
        TransactionsResponseDto transactionsResponseDto = new TransactionsResponseDto();
        when(bankingService.getTransactions(fromDate, toDate)).thenReturn(transactionsResponseDto);

        ResponseEntity<TransactionsResponseDto> response = bankingController.accountTransactions(fromDate, toDate);

        assertEquals(ResponseEntity.ok(transactionsResponseDto), response);
        verify(bankingService).getTransactions(fromDate, toDate);
    }

    @Test
    void moneyTransfer_ShouldCreateTransfer() {
        TransferRequestDto transferRequestDto = new TransferRequestDto();
        TransferResponseDto transferResponseDto = new TransferResponseDto();
        when(bankingService.createMoneyTransefer(transferRequestDto)).thenReturn(transferResponseDto);

        ResponseEntity<TransferResponseDto> response = bankingController.moneyTransfer(transferRequestDto);

        assertEquals(ResponseEntity.ok(transferResponseDto), response);
        verify(bankingService).createMoneyTransefer(transferRequestDto);
    }

}
