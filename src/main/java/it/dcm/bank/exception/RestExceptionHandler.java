package it.dcm.bank.exception;

import it.dcm.bank.generated.dto.ProblemDto;
import it.dcm.bank.mapper.ErrorMapper;
import it.dcm.bank.mapper.ErrorMapperImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;


@RestControllerAdvice
public class RestExceptionHandler {
    private final ErrorMapper errorMapper = new ErrorMapperImpl();
    @ExceptionHandler({HttpClientErrorException.class, HttpServerErrorException.class})
    public ResponseEntity<Object> handleRestClientException(HttpClientErrorException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
    }


    @ExceptionHandler(ResponseClientException.class)
    public ResponseEntity<ProblemDto> handleRestClientException(ResponseClientException ex) {
        ProblemDto problemDto = new ProblemDto();

        problemDto.setStatus(ProblemDto.StatusEnum.KO);
        problemDto.setDescription(ex.getMessage());
        if (ex.getErrors() != null) {
            problemDto.setErrors(ex.getErrors().parallelStream().map(errorMapper::toProblemError).toList());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDto);
    }



}
