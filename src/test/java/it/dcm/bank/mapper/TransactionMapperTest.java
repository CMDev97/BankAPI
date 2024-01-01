package it.dcm.bank.mapper;

import it.dcm.bank.entity.TransactionHistoryEntity;
import it.dcm.bank.generated.client.fabrick.v4.dto.AccountTransactionDto;
import it.dcm.bank.generated.client.fabrick.v4.dto.TypeTransactionDto;
import it.dcm.bank.generated.dto.TransactionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TransactionMapperTest {

    private TransactionMapper transactionMapper;

    @BeforeEach
    void setUp() {
        transactionMapper = Mappers.getMapper(TransactionMapper.class);
    }

    @Test
    void shouldMapFromClientToTransactionDto() {
        AccountTransactionDto sourceDto = new AccountTransactionDto();
        LocalDate date = LocalDate.of(2023, 12, 25);

        sourceDto.setTransactionId("ABC-transaction");
        sourceDto.setOperationId("ABC-operation");
        sourceDto.setAmount(new BigDecimal("23.34"));
        sourceDto.setCurrency("EUR");

        TypeTransactionDto type = new TypeTransactionDto();
        type.setValue("ABC");
        type.setEnumeration("ABC-1");

        sourceDto.setType(type);
        sourceDto.setDescription("Description transaction");
        sourceDto.setAccountingDate(date);
        sourceDto.setValueDate(date);

        TransactionDto mappedDto = transactionMapper.mapFromClient(sourceDto);

        assertEquals(type.getValue(), mappedDto.getTypeTransaction());
        assertEquals(sourceDto.getCurrency(), mappedDto.getCurrency());
        assertEquals(sourceDto.getTransactionId(), mappedDto.getTransactionId());
        assertEquals(sourceDto.getAmount(), mappedDto.getAmount());
        assertEquals(sourceDto.getDescription(), mappedDto.getDescription());
        assertEquals(sourceDto.getOperationId(), mappedDto.getOperationId());
        assertEquals(sourceDto.getAccountingDate(), mappedDto.getAccountingDate());
        assertEquals(sourceDto.getValueDate(), mappedDto.getValueDate());
    }

    @Test
    void toEntityShouldMapTransactionDtoToEntity() {
        TransactionDto dto = new TransactionDto();
        LocalDate date = LocalDate.of(2023, 12, 25);

        dto.setTransactionId("ABC-transaction");
        dto.setOperationId("ABC-operation");
        dto.setAmount(new BigDecimal("23.34"));
        dto.setCurrency("EUR");

        dto.setTypeTransaction("TYPE_TEST");
        dto.setDescription("Description transaction");
        dto.setAccountingDate(date);
        dto.setValueDate(date);

        TransactionHistoryEntity entity = transactionMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getTransactionId(), entity.getId());
        assertEquals(dto.getTypeTransaction(), entity.getTypeTransaction());
        assertEquals(dto.getCurrency(), entity.getCurrency());
        assertEquals(dto.getAmount(), entity.getAmount());
        assertEquals(dto.getDescription(), entity.getDescription());
        assertEquals(dto.getOperationId(), entity.getOperationId());
        assertEquals(dto.getAccountingDate(), entity.getAccountingDate());
        assertEquals(dto.getValueDate(), entity.getValueDate());

    }
}
