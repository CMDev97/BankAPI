package it.dcm.bank.exception;

import ch.qos.logback.core.util.ContentTypeUtil;
import com.fasterxml.jackson.databind.JsonMappingException;
import it.dcm.bank.generated.dto.ErrorItemDto;
import it.dcm.bank.generated.dto.ProblemDto;
import it.dcm.bank.mapper.ErrorMapper;
import it.dcm.bank.mapper.ErrorMapperImpl;
import jdk.jfr.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.function.ServerRequest;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {
    private final ErrorMapper errorMapper = new ErrorMapperImpl();

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDto> handleJsonMappingException(MethodArgumentNotValidException exception) {
        log.error("PROBLEM WITH REQUEST {}", exception.getMessage());
        final ProblemDto problem = new ProblemDto();
        problem.setStatus(ProblemDto.StatusEnum.KO);
        problem.setDescription("Validation error");

        List<ErrorItemDto> errors = exception.getBindingResult().getFieldErrors()
                .parallelStream()
                .map(fieldError -> {
                    ErrorItemDto errorItem = new ErrorItemDto();
                    errorItem.setCode("INPUT_NOT_VALID");
                    errorItem.setDescription(fieldError.getField().concat(" : ").concat(fieldError.getDefaultMessage()));
                    return errorItem;
                }).toList();


        problem.setErrors(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                .body(problem);

    }
    @ExceptionHandler({HttpClientErrorException.class, HttpServerErrorException.class})
    public ResponseEntity<Object> handleClientException(HttpClientErrorException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
    }


    @ExceptionHandler(ResponseClientException.class)
    public ResponseEntity<ProblemDto> handleResponseClientException(ResponseClientException ex) {
        ProblemDto problemDto = new ProblemDto();

        problemDto.setStatus(ProblemDto.StatusEnum.KO);
        problemDto.setDescription(ex.getMessage());
        if (ex.getErrors() != null) {
            problemDto.setErrors(ex.getErrors().parallelStream().map(errorMapper::toProblemError).toList());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDto);
    }



}
