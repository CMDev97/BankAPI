package it.dcm.bank.exception;



import it.dcm.bank.generated.client.fabrick.v4.dto.ErrorItemDto;
import lombok.Getter;

import java.util.List;

@Getter
public class ResponseClientException extends RuntimeException {
    private final List<ErrorItemDto> errors;

    public ResponseClientException(List<ErrorItemDto> errors) {
        super("API call resulted in an error");
        this.errors = errors;
    }
}
