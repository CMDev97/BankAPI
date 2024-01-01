package it.dcm.bank.service;

import it.dcm.bank.client.FabrickClient;
import it.dcm.bank.exception.ValidationException;
import it.dcm.bank.generated.client.fabrick.v4.dto.*;
import it.dcm.bank.generated.dto.*;
import it.dcm.bank.mapper.BalanceMapper;
import it.dcm.bank.mapper.TransactionMapper;
import it.dcm.bank.mapper.TransferMapper;
import it.dcm.bank.service.impl.BankingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static it.dcm.bank.exception.ExceptionTypeEnum.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class BankingServiceImplTest {
    private static final LocalDate DATE_TEST = LocalDate.of(2023, 12, 25);
    private TransactionsResponseDto transactionsResponseOK;
    private AccountTransactionsDto accountTransactionsOK;
    private TransferResponseDto transferResponseOK;
    @Mock
    private FabrickClient fabrickClient;
    @Mock
    private BalanceMapper balanceMapper;
    @Mock
    private TransactionMapper transactionMapper;
    @Mock
    private TransferMapper transferMapper;
    @Mock
    private TransactionHistoryService transactionHistoryService;
    @InjectMocks
    private BankingServiceImpl bankingService;

    @BeforeEach
    void setUp() {
        initModel();
        doNothing().when(transactionHistoryService).saveAllTransactions(anyList());
    }

    @Test
    void getBalance_ShouldReturnMappedBalance() {
        AccountBalanceDto accountBalanceDto = new AccountBalanceDto();
        BalanceResponseDto balanceResponseDto = new BalanceResponseDto();
        balanceResponseDto.setAvailableBalance(100.0F);
        balanceResponseDto.setBalance(100.0F);
        balanceResponseDto.setCurrency("EUR");
        balanceResponseDto.setDate(DATE_TEST);

        when(fabrickClient.getAccountBalance(anyString())).thenReturn(accountBalanceDto);
        when(balanceMapper.mapFromClient(accountBalanceDto)).thenReturn(balanceResponseDto);

        BalanceResponseDto result = bankingService.getBalance();

        assertEquals(balanceResponseDto.getAvailableBalance(), result.getAvailableBalance());
        assertEquals(balanceResponseDto.getBalance(), result.getBalance());
        assertEquals(balanceResponseDto.getCurrency(), result.getCurrency());
        assertEquals(balanceResponseDto.getDate(), result.getDate());

        verify(fabrickClient, timeout(500).times(1)).getAccountBalance(anyString());
        verify(balanceMapper, timeout(500).times(1)).mapFromClient(accountBalanceDto);
    }

    @Test
    void getTransactions_ShouldReturnMappedTransactions() {
        LocalDate from = LocalDate.now();
        LocalDate to = LocalDate.now().plusDays(10);

        when(fabrickClient.getAllTransactions(anyString(), eq(from), eq(to))).thenReturn(accountTransactionsOK);
        when(transactionMapper.mapFromClient(any())).thenReturn(transactionsResponseOK.getList().get(0));

        TransactionsResponseDto result = bankingService.getTransactions(from, to);

        assertNotNull(result);
        assertNotNull(result.getList());
        assertFalse(result.getList().isEmpty());

        verify(fabrickClient, timeout(500).times(1)).getAllTransactions(anyString(), eq(from), eq(to));
        verify(transactionMapper, timeout(500).times(1)).mapFromClient(any());
    }

    @Test
    void getTransactions_ShouldThrowValidationException_WhenToBeforeFromDate() {
        LocalDate from = LocalDate.now();
        LocalDate to = LocalDate.now().minusDays(10);

        ValidationException ex = assertThrows(ValidationException.class, () -> bankingService.getTransactions(from, to));

        assertEquals(RANGE_INTERVAL_NOT_VALID.getCode(), ex.getCode());
        assertEquals(RANGE_INTERVAL_NOT_VALID.getMessage(), ex.getMessage());

        verify(fabrickClient, timeout(500).times(0)).getAllTransactions(anyString(), eq(from), eq(to));
    }


    @Test
    void getTransactions_ShouldReturnEmptyTransactions_WhenClientResponseIsNull() {
        LocalDate from = LocalDate.now();
        LocalDate to = LocalDate.now().plusDays(10);
        when(fabrickClient.getAllTransactions(anyString(), eq(from), eq(to))).thenReturn(null);

        TransactionsResponseDto result = bankingService.getTransactions(from, to);

        assertNotNull(result);

        verify(fabrickClient, timeout(500).times(1)).getAllTransactions(anyString(), eq(from), eq(to));
    }

    @Test
    void createMoneyTransfer_ShouldReturnTransferResponseDTO() {
        TransferRequestDto transferRequestDto = new TransferRequestDto();
        transferRequestDto.setIsInstant(true);

        MoneyTransferRequestDto requestDto = new MoneyTransferRequestDto();
        when(transferMapper.toClientRequest(any())).thenReturn(requestDto);

        MoneyTransferDto responseClient = new MoneyTransferDto();
        when(fabrickClient.createMoneyTransfer(anyString(), any())).thenReturn(responseClient);

        when(transferMapper.mapClientResponse(responseClient)).thenReturn(transferResponseOK);

        TransferResponseDto result = bankingService.createMoneyTransefer(transferRequestDto);

        assertNotNull(result);
        assertEquals(transferResponseOK.getMoneyTransferId(), result.getMoneyTransferId());
        assertEquals(transferResponseOK.getStatus(), result.getStatus());


        verify(transferMapper, timeout(500).times(1)).mapClientResponse(any());
        verify(transferMapper, timeout(500).times(1)).toClientRequest(any());
        verify(fabrickClient, timeout(500).times(1)).createMoneyTransfer(anyString(), any());

    }

    @Test
    void createMoneyTransfer_ShouldHandleValidation_WhenInstantFalseAndExecutionDateIsNull() {
        TransferRequestDto transferRequestDto = new TransferRequestDto();
        transferRequestDto.setIsInstant(false);
        transferRequestDto.setExecutionDate(null);

        ValidationException ex = assertThrows(ValidationException.class, () -> bankingService.createMoneyTransefer(transferRequestDto));
        assertEquals(INPUT_MISSING_DATA.getCode(), ex.getCode());
        assertEquals(INPUT_MISSING_DATA.getMessage().concat(" execution date is null"), ex.getMessage());

        verify(transferMapper, timeout(500).times(0)).mapClientResponse(any());
        verify(transferMapper, timeout(500).times(0)).toClientRequest(any());
        verify(fabrickClient, timeout(500).times(0)).createMoneyTransfer(anyString(), any());

    }


    @Test
    void createMoneyTransfer_ShouldHandleValidation_WhenInstantFalseAndExecutionDateIsBeforeNow() {
        TransferRequestDto transferRequestDto = new TransferRequestDto();
        transferRequestDto.setIsInstant(false);
        transferRequestDto.setExecutionDate(LocalDate.now().minusDays(10));

        ValidationException ex = assertThrows(ValidationException.class, () -> bankingService.createMoneyTransefer(transferRequestDto));
        assertEquals(INVALID_DATE.getCode(), ex.getCode());
        assertEquals(INVALID_DATE.getMessage(), ex.getMessage());

        verify(transferMapper, timeout(500).times(0)).mapClientResponse(any());
        verify(transferMapper, timeout(500).times(0)).toClientRequest(any());
        verify(fabrickClient, timeout(500).times(0)).createMoneyTransfer(anyString(), any());

    }

    private void initModel() {
        transactionsResponseOK = new TransactionsResponseDto();
        TransactionDto transaction1 = new TransactionDto();
        transaction1.setAmount(new BigDecimal("23.44"));
        transaction1.setCurrency("EUR");
        transaction1.setDescription("Child credit");
        transaction1.setTransactionId("ABC-transaction");
        transaction1.setOperationId("ABC-operation");
        transaction1.setValueDate(DATE_TEST);
        transaction1.setAccountingDate(DATE_TEST);
        transactionsResponseOK.addListItem(transaction1);

        accountTransactionsOK = new AccountTransactionsDto();
        AccountTransactionDto accountTransaction = new AccountTransactionDto();
        accountTransaction.setAmount(new BigDecimal("23.44"));
        accountTransaction.setCurrency("EUR");
        accountTransaction.setDescription("Child credit");
        accountTransaction.setTransactionId("ABC-transaction");
        accountTransaction.setOperationId("ABC-operation");
        accountTransaction.setValueDate(DATE_TEST);
        accountTransaction.setAccountingDate(DATE_TEST);
        accountTransactionsOK.addListItem(accountTransaction);

        transferResponseOK = new TransferResponseDto();
        transferResponseOK.setMoneyTransferId("ABC-transfer");
        transferResponseOK.setStatus(TransferResponseDto.StatusEnum.EXECUTED);
    }
}
