package cn.cccs7.shared.model.util;

import cn.cccs7.shared.model.NoOopsResponse;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Builder class for constructing NoOopsResponse objects
 * @param <T> the type of data in the response
 */
public class NoOopsResponseBuilder<T> {
    
    private Boolean success;
    private String code;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    private LocalDateTime requestedTime;
    private String traceId;
    
    /**
     * Private constructor to prevent direct instantiation
     */
    private NoOopsResponseBuilder() {
        this.timestamp = LocalDateTime.now();
        this.traceId = UUID.randomUUID().toString();
    }
    
    /**
     * Create a new NoOopsResponseBuilder instance
     *
     * @param <T> the type of data in the response
     * @return a new NoOopsResponseBuilder instance
     */
    public static <T> NoOopsResponseBuilder<T> builder() {
        return new NoOopsResponseBuilder<>();
    }
    
    /**
     * Set the success flag
     *
     * @param success success flag
     * @return this builder instance
     */
    public NoOopsResponseBuilder<T> success(Boolean success) {
        this.success = success;
        return this;
    }
    
    /**
     * Set the response code
     *
     * @param code response code
     * @return this builder instance
     */
    public NoOopsResponseBuilder<T> code(String code) {
        this.code = code;
        return this;
    }
    
    /**
     * Set the response message
     *
     * @param message response message
     * @return this builder instance
     */
    public NoOopsResponseBuilder<T> message(String message) {
        this.message = message;
        return this;
    }
    
    /**
     * Set the response data
     *
     * @param data response data
     * @return this builder instance
     */
    public NoOopsResponseBuilder<T> data(T data) {
        this.data = data;
        return this;
    }
    
    /**
     * Set the timestamp
     *
     * @param timestamp timestamp
     * @return this builder instance
     */
    public NoOopsResponseBuilder<T> timestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }
    
    /**
     * Set the requested time
     *
     * @param requestedTime requested time
     * @return this builder instance
     */
    public NoOopsResponseBuilder<T> requestedTime(LocalDateTime requestedTime) {
        this.requestedTime = requestedTime;
        return this;
    }
    
    /**
     * Set the trace ID
     *
     * @param traceId trace ID
     * @return this builder instance
     */
    public NoOopsResponseBuilder<T> traceId(String traceId) {
        this.traceId = traceId;
        return this;
    }
    
    /**
     * Build the NoOopsResponse object
     *
     * @return the built NoOopsResponse object
     */
    public NoOopsResponse<T> build() {
        NoOopsResponse<T> response = new NoOopsResponse<>();
        response.setSuccess(success);
        response.setCode(code);
        response.setMessage(message);
        response.setData(data);
        response.setTimestamp(timestamp);
        response.setRequestedTime(requestedTime);
        response.setTraceId(traceId);
        return response;
    }
    
    /**
     * Build a successful response with data
     *
     * @param data response data
     * @return the built NoOopsResponse object
     */
    public static <T> NoOopsResponse<T> success(T data) {
        NoOopsResponse<T> response = new NoOopsResponse<>();
        response.setSuccess(true);
        response.setCode(String.valueOf(HttpStatus.OK.value()));
        response.setMessage(HttpStatus.OK.getReasonPhrase());
        response.setData(data);
        response.setTimestamp(LocalDateTime.now());
        response.setTraceId(UUID.randomUUID().toString());
        return response;
    }
    
    /**
     * Build a successful response with message
     *
     * @param message response message
     * @return the built NoOopsResponse object
     */
    public static <T> NoOopsResponse<T> successWithMessage(String message) {
        NoOopsResponse<T> response = new NoOopsResponse<>();
        response.setSuccess(true);
        response.setCode(String.valueOf(HttpStatus.OK.value()));
        response.setMessage(message);
        response.setTimestamp(LocalDateTime.now());
        response.setTraceId(UUID.randomUUID().toString());
        return response;
    }
    
    /**
     * Build a successful response with data and message
     *
     * @param data response data
     * @param message response message
     * @return the built NoOopsResponse object
     */
    public static <T> NoOopsResponse<T> success(T data, String message) {
        NoOopsResponse<T> response = new NoOopsResponse<>();
        response.setSuccess(true);
        response.setCode(String.valueOf(HttpStatus.OK.value()));
        response.setMessage(message);
        response.setData(data);
        response.setTimestamp(LocalDateTime.now());
        response.setTraceId(UUID.randomUUID().toString());
        return response;
    }
    
    /**
     * Build an error response
     *
     * @param message error message
     * @return the built NoOopsResponse object
     */
    public static <T> NoOopsResponse<T> error(String message) {
        NoOopsResponse<T> response = new NoOopsResponse<>();
        response.setSuccess(false);
        response.setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        response.setMessage(message);
        response.setTimestamp(LocalDateTime.now());
        response.setTraceId(UUID.randomUUID().toString());
        return response;
    }
    
    /**
     * Build an error response with specific HTTP status
     *
     * @param message error message
     * @param status HTTP status
     * @return the built NoOopsResponse object
     */
    public static <T> NoOopsResponse<T> error(String message, HttpStatus status) {
        NoOopsResponse<T> response = new NoOopsResponse<>();
        response.setSuccess(false);
        response.setCode(String.valueOf(status.value()));
        response.setMessage(message);
        response.setTimestamp(LocalDateTime.now());
        response.setTraceId(UUID.randomUUID().toString());
        return response;
    }
    
    /**
     * Build an error response with code and message
     *
     * @param code error code
     * @param message error message
     * @return the built NoOopsResponse object
     */
    public static <T> NoOopsResponse<T> error(String code, String message) {
        NoOopsResponse<T> response = new NoOopsResponse<>();
        response.setSuccess(false);
        response.setCode(code);
        response.setMessage(message);
        response.setTimestamp(LocalDateTime.now());
        response.setTraceId(UUID.randomUUID().toString());
        return response;
    }
}