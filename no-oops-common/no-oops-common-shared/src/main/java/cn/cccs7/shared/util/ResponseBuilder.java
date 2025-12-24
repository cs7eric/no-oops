package cn.cccs7.shared.util;

import cn.cccs7.shared.model.Response;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * Builder class for constructing Response objects
 * @param <T> the type of data in the response
 */
public class ResponseBuilder<T> {
    
    private Boolean success;
    private String code;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    
    /**
     * Private constructor to prevent direct instantiation
     */
    private ResponseBuilder() {
        this.timestamp = LocalDateTime.now();
    }
    
    /**
     * Create a new ResponseBuilder instance
     *
     * @param <T> the type of data in the response
     * @return a new ResponseBuilder instance
     */
    public static <T> ResponseBuilder<T> builder() {
        return new ResponseBuilder<>();
    }
    
    /**
     * Set the success flag
     *
     * @param success success flag
     * @return this builder instance
     */
    public ResponseBuilder<T> success(Boolean success) {
        this.success = success;
        return this;
    }
    
    /**
     * Set the response code
     *
     * @param code response code
     * @return this builder instance
     */
    public ResponseBuilder<T> code(String code) {
        this.code = code;
        return this;
    }
    
    /**
     * Set the response message
     *
     * @param message response message
     * @return this builder instance
     */
    public ResponseBuilder<T> message(String message) {
        this.message = message;
        return this;
    }
    
    /**
     * Set the response data
     *
     * @param data response data
     * @return this builder instance
     */
    public ResponseBuilder<T> data(T data) {
        this.data = data;
        return this;
    }
    
    /**
     * Set the timestamp
     *
     * @param timestamp timestamp
     * @return this builder instance
     */
    public ResponseBuilder<T> timestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }
    
    /**
     * Build the Response object
     *
     * @return the built Response object
     */
    public Response<T> build() {
        return Response.<T>builder()
                .success(success)
                .code(code)
                .message(message)
                .data(data)
                .timestamp(timestamp)
                .build();
    }
    
    /**
     * Build a successful response with data
     *
     * @param data response data
     * @return the built Response object
     */
    public static <T> Response<T> success(T data) {
        return Response.<T>builder()
                .success(true)
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.getReasonPhrase())
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Build a successful response with message
     *
     * @param message response message
     * @return the built Response object
     */
    public static <T> Response<T> successWithMessage(String message) {
        return Response.<T>builder()
                .success(true)
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Build a successful response with data and message
     *
     * @param data response data
     * @param message response message
     * @return the built Response object
     */
    public static <T> Response<T> success(T data, String message) {
        return Response.<T>builder()
                .success(true)
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Build an error response
     *
     * @param message error message
     * @return the built Response object
     */
    public static <T> Response<T> error(String message) {
        return Response.<T>builder()
                .success(false)
                .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Build an error response with specific HTTP status
     *
     * @param message error message
     * @param status HTTP status
     * @return the built Response object
     */
    public static <T> Response<T> error(String message, HttpStatus status) {
        return Response.<T>builder()
                .success(false)
                .code(String.valueOf(status.value()))
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Build an error response with code and message
     *
     * @param code error code
     * @param message error message
     * @return the built Response object
     */
    public static <T> Response<T> error(String code, String message) {
        return Response.<T>builder()
                .success(false)
                .code(code)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}