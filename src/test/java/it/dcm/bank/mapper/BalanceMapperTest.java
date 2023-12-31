package it.dcm.bank.mapper;

import it.dcm.bank.generated.client.fabrick.v4.dto.AccountBalanceDto;
import it.dcm.bank.generated.dto.BalanceResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BalanceMapperTest {

    private BalanceMapper balanceMapper;

    @BeforeEach
    void setUp() {
        balanceMapper = Mappers.getMapper(BalanceMapper.class);
    }

    @Test
    void shouldMapFromClientToBalanceResponseDto() {
        LocalDate date = LocalDate.of(2023, 12, 25);

        AccountBalanceDto sourceDto = new AccountBalanceDto();
        sourceDto.setAvailableBalance(100.0F);
        sourceDto.setBalance(100.0F);
        sourceDto.setCurrency("EUR");
        sourceDto.setDate(date);

        BalanceResponseDto mappedDto = balanceMapper.mapFromClient(sourceDto);

        assertEquals(sourceDto.getAvailableBalance(), mappedDto.getAvailableBalance());
        assertEquals(sourceDto.getCurrency(), mappedDto.getCurrency());
        assertEquals(sourceDto.getDate(), mappedDto.getDate());
        assertEquals(sourceDto.getBalance(), mappedDto.getBalance());

    }
}
