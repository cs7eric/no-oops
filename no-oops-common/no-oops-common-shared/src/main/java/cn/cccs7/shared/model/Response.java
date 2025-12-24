package cn.cccs7.shared.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Success flag
     */
    private Boolean success;
    
    /**
     * Response code
     */
    private String code;
    
    /**
     * Response message
     */
    private String message;
    
    /**
     * Response data
     */
    private T data;
    
    /**
     * Timestamp
     */
    private LocalDateTime timestamp;
}