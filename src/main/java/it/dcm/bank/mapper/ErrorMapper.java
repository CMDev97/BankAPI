package it.dcm.bank.mapper;


import it.dcm.bank.generated.dto.ErrorItemDto;
import org.mapstruct.Mapper;

@Mapper
public interface ErrorMapper {

    ErrorItemDto toProblemError(it.dcm.bank.generated.client.fabrick.v4.dto.ErrorItemDto dto);

}
