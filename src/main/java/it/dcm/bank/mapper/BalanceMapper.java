package it.dcm.bank.mapper;



import it.dcm.bank.generated.client.fabrick.v4.dto.AccountBalanceDto;
import it.dcm.bank.generated.dto.BalanceResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BalanceMapper {

    BalanceResponseDto mapFromClient(AccountBalanceDto dto);

}
