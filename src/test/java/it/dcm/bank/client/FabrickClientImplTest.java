package it.dcm.bank.client;

import it.dcm.bank.client.impl.FabrickClientImpl;
import it.dcm.bank.exception.ResponseClientException;
import it.dcm.bank.generated.client.fabrick.v4.api.BankAccountApi;
import it.dcm.bank.generated.client.fabrick.v4.api.PaymentsMoneyTransferApi;
import it.dcm.bank.generated.client.fabrick.v4.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FabrickClientImplTest {
    private final String testAccountId = "12345";
    private final LocalDate testFromDate = LocalDate.of(2023, 10, 1);
    private final LocalDate testToDate = LocalDate.of(2023, 12, 25);
    private MoneyTransferRequestDto testRequest;


    @Mock
    private BankAccountApi bankAccountApi;

    @Mock
    private PaymentsMoneyTransferApi paymentsMoneyTransferApi;

    @InjectMocks
    private FabrickClientImpl fabrickClient;


    @BeforeEach
    void setUp(){
        this.init();
    }


    @Test
    void getAccountBalance_ShouldReturnBalance_WhenApiCallIsSuccessful() {
        AccountBalanceResponseDto mockResponse = new AccountBalanceResponseDto();
        mockResponse.setStatus(AccountBalanceResponseDto.StatusEnum.OK);
        AccountBalanceDto mockPayload = new AccountBalanceDto();
        mockResponse.setPayload(mockPayload);

        when(bankAccountApi.cashAccountBalance(testAccountId)).thenReturn(mockResponse);

        AccountBalanceDto result = fabrickClient.getAccountBalance(testAccountId);

        assertEquals(mockPayload, result);
        verify(bankAccountApi).cashAccountBalance(testAccountId);
    }

    @Test
    void getAccountBalance_ShouldThrowException_WhenApiResponseIsKO() {
        AccountBalanceResponseDto mockResponse = new AccountBalanceResponseDto();
        mockResponse.setStatus(AccountBalanceResponseDto.StatusEnum.KO);

        when(bankAccountApi.cashAccountBalance(testAccountId)).thenReturn(mockResponse);

        assertThrows(ResponseClientException.class, () -> fabrickClient.getAccountBalance(testAccountId));
    }

    @Test
    void getAccountBalance_ShouldThrowException_WhenHttpClientErrorExceptionIsThrown() {
        when(bankAccountApi.cashAccountBalance(testAccountId)).thenThrow(HttpClientErrorException.class);

        assertThrows(HttpClientErrorException.class, () -> fabrickClient.getAccountBalance(testAccountId));
    }

    @Test
    void getAllTransactions_ShouldReturnTransactions_WhenApiCallIsSuccessful() {
        AccountTransactionsResponseDto mockResponse = new AccountTransactionsResponseDto();
        mockResponse.setStatus(AccountTransactionsResponseDto.StatusEnum.OK);
        AccountTransactionsDto mockPayload = new AccountTransactionsDto();
        mockResponse.setPayload(mockPayload);

        when(bankAccountApi.cashAccountTransactions(testAccountId, testFromDate, testToDate)).thenReturn(mockResponse);

        AccountTransactionsDto result = fabrickClient.getAllTransactions(testAccountId, testFromDate, testToDate);

        assertEquals(mockPayload, result);
        verify(bankAccountApi).cashAccountTransactions(testAccountId, testFromDate, testToDate);
    }

    @Test
    void getAllTransactions_ShouldThrowException_WhenApiResponseIsKO() {
        AccountTransactionsResponseDto mockResponse = new AccountTransactionsResponseDto();
        mockResponse.setStatus(AccountTransactionsResponseDto.StatusEnum.KO);

        when(bankAccountApi.cashAccountTransactions(testAccountId, testFromDate, testToDate)).thenReturn(mockResponse);

        assertThrows(ResponseClientException.class, () -> fabrickClient.getAllTransactions(testAccountId, testFromDate, testToDate));
    }

    @Test
    void getAllTransactions_ShouldThrowException_WhenHttpClientErrorExceptionIsThrown() {
        when(bankAccountApi.cashAccountTransactions(testAccountId, testFromDate, testToDate)).thenThrow(HttpClientErrorException.class);

        assertThrows(HttpClientErrorException.class, () -> fabrickClient.getAllTransactions(testAccountId, testFromDate, testToDate));
    }

    @Test
    void createMoneyTransfer_ShouldReturnMoneyTransfer_WhenApiCallIsSuccessful() {
        MoneyTransferResponseDto mockResponse = new MoneyTransferResponseDto();
        mockResponse.setStatus(MoneyTransferResponseDto.StatusEnum.OK);
        MoneyTransferDto mockPayload = new MoneyTransferDto();
        mockPayload.setStatus(MoneyTransferDto.StatusEnum.EXECUTED);
        mockPayload.setMoneyTransferId("12345678-OPERATION");
        mockResponse.setPayload(mockPayload);

        when(paymentsMoneyTransferApi.createMoneyTransfer(testAccountId, testRequest)).thenReturn(mockResponse);

        MoneyTransferDto result = fabrickClient.createMoneyTransfer(testAccountId, testRequest);

        assertEquals(mockPayload, result);
        verify(paymentsMoneyTransferApi).createMoneyTransfer(testAccountId, testRequest);
    }

    @Test
    void createMoneyTransfer_ShouldThrowException_WhenApiResponseIsKO() {
        MoneyTransferResponseDto mockResponse = new MoneyTransferResponseDto();
        mockResponse.setStatus(MoneyTransferResponseDto.StatusEnum.KO);

        when(paymentsMoneyTransferApi.createMoneyTransfer(testAccountId, testRequest)).thenReturn(mockResponse);

        assertThrows(ResponseClientException.class, () -> fabrickClient.createMoneyTransfer(testAccountId, testRequest));
    }

    @Test
    void createMoneyTransfer_ShouldThrowException_WhenHttpClientErrorExceptionIsThrown() {
        when(paymentsMoneyTransferApi.createMoneyTransfer(testAccountId, testRequest)).thenThrow(HttpClientErrorException.class);

        assertThrows(HttpClientErrorException.class, () -> fabrickClient.createMoneyTransfer(testAccountId, testRequest));
    }


    private void init(){
        this.testRequest = new MoneyTransferRequestDto();
        this.testRequest.setAmount(new BigDecimal(300));
        this.testRequest.setCurrency("EUR");
        this.testRequest.setUri("IMPORTANT");
        this.testRequest.setDescription("PAYMENT UNIVERSITY TAX");
        this.testRequest.setExecutionDate(testFromDate);
        CreditorDto creditorDto = new CreditorDto();
        creditorDto.setName("John doe");
        CreditorAccountDto creditorAccountDto = new CreditorAccountDto();
        creditorAccountDto.setAccountCode("IT23A0336844430152923804660");
        creditorAccountDto.setBicCode("SELBIT2BXXX");
        creditorDto.setAccount(creditorAccountDto);
        this.testRequest.setCreditor(creditorDto);
    }

}
