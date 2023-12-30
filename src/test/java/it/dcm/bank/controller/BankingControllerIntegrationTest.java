package it.dcm.bank.controller;

import it.dcm.bank.generated.dto.*;
import it.dcm.bank.service.BankingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BankingController.class)
class BankingControllerIntegrationTest {
    private BalanceResponseDto responseBalanceOK;
    private TransactionsResponseDto transactionsResponseOK;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankingService bankingService;

    @BeforeEach
    void setUp() {
        initModel();
        TransferResponseDto transferResponseDto = new TransferResponseDto();
        when(bankingService.createMoneyTransefer(any())).thenReturn(transferResponseDto);
    }

    @Test
    void accountBalance_ShouldReturnBalance() throws Exception {
        when(bankingService.getBalance()).thenReturn(this.responseBalanceOK);

        mockMvc.perform(get("/api/v1.0/account/balance")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(
                        """
                            {
                                "date": "2023-01-23",
                                "balance": 4000.45,
                                "availableBalance": 4000.45,
                                "currency": "EUR"
                            }
                        """
                ));

        verify(bankingService).getBalance();
    }

    @Test
    void accountBalance_ShouldReturnProblem_WhenClientThrowException() throws Exception {
        String errorBody = """
                        {
                            "status": "KO",
                            "errors": [
                                {
                                    "code": "API000",
                                    "description": "Account id not present",
                                    "params": ""
                                }
                            ],
                            "payload": {}
                        }
                """;
        HttpClientErrorException exception = HttpClientErrorException.create(
                HttpStatus.BAD_REQUEST, "Error", null, errorBody.getBytes(), null);

        when(bankingService.getBalance()).thenThrow(exception);

        mockMvc.perform(get("/api/v1.0/account/balance")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(
                        """
                            {
                            "status": "KO",
                            "errors": [
                                {
                                    "code": "API000",
                                    "description": "Account id not present",
                                    "params": ""
                                }
                            ],
                            "payload": {}
                        }
                        """
                ));

        verify(bankingService).getBalance();
    }

    @Test
    void accountTransactions_ShouldReturnTransactions() throws Exception {
        when(bankingService.getTransactions(any(), any())).thenReturn(transactionsResponseOK);

        mockMvc.perform(get("/api/v1.0/account/transactions?fromDate=2022-01-01&toDate=2022-01-31")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                    {
                        list: [
                            {
                                "amount": 23.44,
                                "currency": "EUR",
                                "description": "Child credit",
                                "transactionId": "ABC-transaction",
                                "operationId": "ABC-operation",
                                "valueDate": "2023-01-23",
                                "accountingDate": "2023-01-23"
                            }
                        ]
                    }
                """));

        verify(bankingService).getTransactions(any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void accountTransactions_ShouldReturnProblem_WhenClientThrowException() throws Exception {
        String errorBody = """
                        {
                            "status": "KO",
                            "errors": [
                                {
                                    "code": "API000",
                                    "description": "Error interval Date",
                                    "params": ""
                                }
                            ],
                            "payload": {}
                        }
                """;
        HttpClientErrorException exception = HttpClientErrorException.create(
                HttpStatus.BAD_REQUEST, "Error", null, errorBody.getBytes(), null);

        when(bankingService.getTransactions(any(), any())).thenThrow(exception);

        mockMvc.perform(get("/api/v1.0/account/transactions?fromDate=2022-01-01&toDate=2022-01-31")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(
                        """
                            {
                            "status": "KO",
                            "errors": [
                                {
                                    "code": "API000",
                                    "description": "Error interval Date",
                                    "params": ""
                                }
                            ],
                            "payload": {}
                        }
                        """
                ));

        verify(bankingService).getTransactions(any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void moneyTransfer_ShouldCreateTransfer() throws Exception {
        mockMvc.perform(post("/api/v1.0/account/money-transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "creditorName": "John Doe",
                                "creditorAccountCode": "IT23A0336844430152923804660",
                                "description": "Payment invoice 75/2017",
                                "amount": 800,
                                "currency": "EUR",
                                "creditorBicCode": "SELBIT2BXXX",
                                "executionDate": "2023-12-12",
                                "isUrgent": false,
                                "isInstant": false
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(bankingService).createMoneyTransefer(any(TransferRequestDto.class));
    }

    void initModel(){
        LocalDate date = LocalDate.of(2023, 1, 23);
        responseBalanceOK = new BalanceResponseDto();
        responseBalanceOK.setBalance(4000.45F);
        responseBalanceOK.setAvailableBalance(4000.45F);
        responseBalanceOK.setCurrency("EUR");
        responseBalanceOK.setDate(date);
        transactionsResponseOK = new TransactionsResponseDto();
        TransactionDto transaction1 = new TransactionDto();
        transaction1.setAmount(new BigDecimal("23.44"));
        transaction1.setCurrency("EUR");
        transaction1.setDescription("Child credit");
        transaction1.setTransactionId("ABC-transaction");
        transaction1.setOperationId("ABC-operation");
        transaction1.setValueDate(date);
        transaction1.setAccountingDate(date);
        transactionsResponseOK.addListItem(transaction1);
    }

}
