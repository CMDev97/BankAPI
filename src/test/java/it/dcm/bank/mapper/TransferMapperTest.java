package it.dcm.bank.mapper;

import it.dcm.bank.generated.client.fabrick.v4.dto.MoneyTransferDto;
import it.dcm.bank.generated.client.fabrick.v4.dto.MoneyTransferRequestDto;
import it.dcm.bank.generated.dto.TransferRequestDto;
import it.dcm.bank.generated.dto.TransferResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TransferMapperTest {

    private TransferMapper transferMapper;

    @BeforeEach
    void setUp() {
        transferMapper = Mappers.getMapper(TransferMapper.class);
    }

    @Test
    void toClientRequest_ShouldMapCorrectly() {
        TransferRequestDto requestDto = new TransferRequestDto();
        LocalDate date = LocalDate.of(2023, 12, 25);

        requestDto.setCreditorName("Creditor Name");
        requestDto.setCreditorAccountCode("Account Code");
        requestDto.setCreditorBicCode("BIC Code");
        requestDto.setAmount(new BigDecimal("23.45"));
        requestDto.setDescription("Account transfer");
        requestDto.setCurrency("EUR");
        requestDto.setExecutionDate(date);
        requestDto.setIsInstant(false);
        requestDto.setIsUrgent(false);


        MoneyTransferRequestDto mappedRequest = transferMapper.toClientRequest(requestDto);

        assertEquals(requestDto.getCreditorName(), mappedRequest.getCreditor().getName());
        assertEquals(requestDto.getCreditorAccountCode(), mappedRequest.getCreditor().getAccount().getAccountCode());
        assertEquals(requestDto.getCreditorBicCode(), mappedRequest.getCreditor().getAccount().getBicCode());
        assertEquals(requestDto.getAmount(), mappedRequest.getAmount());
        assertEquals(requestDto.getCurrency(), mappedRequest.getCurrency());
        assertEquals(requestDto.getDescription(), mappedRequest.getDescription());
        assertEquals(requestDto.getExecutionDate(), mappedRequest.getExecutionDate());
        assertEquals(requestDto.getIsInstant(), mappedRequest.getIsInstant());
        assertEquals(requestDto.getIsUrgent(), mappedRequest.getIsUrgent());
    }

    @Test
    void mapClientResponse_ShouldMapCorrectly() {
        MoneyTransferDto responseDto = new MoneyTransferDto();
        responseDto.setMoneyTransferId("ABC-12324");
        responseDto.setStatus(MoneyTransferDto.StatusEnum.EXECUTED);

        TransferResponseDto mappedResponse = transferMapper.mapClientResponse(responseDto);

        assertEquals(responseDto.getMoneyTransferId(), mappedResponse.getMoneyTransferId());
        assertEquals(responseDto.getStatus().getValue(), mappedResponse.getStatus().getValue());
    }
}
