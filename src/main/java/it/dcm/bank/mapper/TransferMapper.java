package it.dcm.bank.mapper;


import it.dcm.bank.generated.client.fabrick.v4.dto.MoneyTransferDto;
import it.dcm.bank.generated.client.fabrick.v4.dto.MoneyTransferRequestDto;
import it.dcm.bank.generated.dto.TransferRequestDto;
import it.dcm.bank.generated.dto.TransferResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransferMapper {


    @Mapping(target = "creditor.name", source= "request.creditorName")
    @Mapping(target = "creditor.account.accountCode", source= "request.creditorAccountCode")
    @Mapping(target = "creditor.account.bicCode", source= "request.creditorBicCode")
    MoneyTransferRequestDto toClientRequest(TransferRequestDto request);


    TransferResponseDto mapClientResponse(MoneyTransferDto response);

}
