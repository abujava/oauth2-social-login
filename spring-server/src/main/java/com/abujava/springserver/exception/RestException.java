package com.abujava.springserver.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class RestException extends RuntimeException {

    private String message;
    private HttpStatus status;

    private RestException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }

    private RestException(String message, int errorCode, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }

    public static RestException restThrow(String userMsg, HttpStatus httpStatus) {
        return new RestException(userMsg, httpStatus);
    }


    public static RestException restThrow(String message) {
        return new RestException(message, HttpStatus.BAD_REQUEST);
    }

    /**
     * @param resourceKey - {@link org.springframework.context.MessageSource} bo'yicha kelishi kerak. Masalan "GROUP"
     * @return Guruh topilmadi!
     */
    public static RestException notFound(String element) {
        return new RestException(
                String.format("%s not found!", element),
                HttpStatus.NOT_FOUND
        );
    }

    public static RestException badRequest(String message) {
        return new RestException(
                message,
                HttpStatus.BAD_REQUEST
        );
    }

    public static RestException required(String element) {
        return new RestException(
                String.format("%s required!", element),
                HttpStatus.BAD_REQUEST
        );
    }

    public static RestException alreadyExists(String element) {
        return new RestException(
                String.format("%s already exists!", element),
                HttpStatus.CONFLICT
        );
    }

    public static RestException forbidden() {
        return new RestException(
                "Access is not possible!",
                HttpStatus.FORBIDDEN
        );
    }

    public static RestException internalServerError() {
        return new RestException(
                "Internal server error",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
