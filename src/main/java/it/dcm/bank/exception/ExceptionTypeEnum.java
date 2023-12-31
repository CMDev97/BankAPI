package it.dcm.bank.exception;


import lombok.Getter;

public enum ExceptionTypeEnum {
    INPUT_MISSING_DATA("API999", "Error: missing parameter(s)"),
    INVALID_DATE("API349", "Error: date is illegal format or not valid"),
    RANGE_INTERVAL_NOT_VALID("API348", "Error: data interval is not valid")
    ;

    private final String code;
    private final String message;

    ExceptionTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
