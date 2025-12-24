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
public class NoOopsResponse<T> implements Serializable {
    
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
    
    private LocalDateTime requestedTime;
    
    private String traceId;
    
    /**
     * Check if the response is successful
     * @return true if successful, false otherwise
     */
    public boolean isSuccess() {
        return Boolean.TRUE.equals(this.success);
    }
    
    /**
     * Check if the response is failed
     * @return true if failed, false otherwise
     */
    public boolean isFailure() {
        return !isSuccess();
    }
    
    // 添加 setter 方法以解决编译错误
    public void setSuccess(Boolean success) {
        this.success = success;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public void setRequestedTime(LocalDateTime requestedTime) {
        this.requestedTime = requestedTime;
    }
    
    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}