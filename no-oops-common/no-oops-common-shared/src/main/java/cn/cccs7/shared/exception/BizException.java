package cn.cccs7.shared.exception;

import lombok.Getter;

/**
 * Custom Business Exception for handling expected business logic failures.
 * This exception is caught by the GlobalExceptionHandler to return a unified Response.
 */
@Getter
public class BizException extends RuntimeException {
    
    private final String code;
    private final String message;

    /**
     * Constructor with explicit code and message
     * @param code    The business error code
     * @param message The user-friendly error message
     */
    public BizException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    /**
     * Optional: Constructor for wrapping an existing Throwable with a business context
     */
    public BizException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }
    
    /**
     * Get the error code
     * @return error code
     */
    public String getCode() {
        return code;
    }
    
    /**
     * Get the error message
     * @return error message
     */
    @Override
    public String getMessage() {
        return message;
    }
}