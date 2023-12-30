package it.dcm.bank.mapper;

import it.dcm.bank.generated.dto.ErrorItemDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class ErrorMapperTest {

    private ErrorMapper errorMapper;

    @BeforeEach
    void setUp() {
        errorMapper = Mappers.getMapper(ErrorMapper.class);
    }

    @Test
    void toProblemError_ShouldCorrectlyMapFields() {
        it.dcm.bank.generated.client.fabrick.v4.dto.ErrorItemDto sourceDto = new it.dcm.bank.generated.client.fabrick.v4.dto.ErrorItemDto();
        sourceDto.setCode("API000");
        sourceDto.setDescription("AccountId not present");

        // Mappa il DTO
        ErrorItemDto mappedDto = errorMapper.toProblemError(sourceDto);

        // Verifica che i campi siano mappati correttamente
        assertEquals(sourceDto.getCode(), mappedDto.getCode());
        assertEquals(sourceDto.getDescription(), mappedDto.getDescription());
    }
}
