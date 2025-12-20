package cn.cccs7.shared.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Base exception class for the application
 */
@Getter
public class BaseException extends RuntimeException {
    
    private final String code;
    private final HttpStatus status;
    
    public BaseException(String message) {
        super(message);
        this.code = "500";
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }
    
    public BaseException(String code, String message) {
        super(message);
        this.code = code;
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }
    
    public BaseException(String code, String message, HttpStatus status) {
        super(message);
        this.code = code;
        this.status = status;
    }
    
    public BaseException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }
    
    public BaseException(String code, String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.status = status;
    }
}