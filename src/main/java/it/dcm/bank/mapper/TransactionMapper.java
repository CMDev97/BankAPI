package it.dcm.bank.mapper;



import it.dcm.bank.generated.client.fabrick.v4.dto.AccountTransactionDto;
import it.dcm.bank.generated.dto.TransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {


    @Mapping(target = "typeTransaction", source= "dto.type.value")
    TransactionDto mapFromClient(AccountTransactionDto dto);


}
