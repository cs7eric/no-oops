package cn.cccs7.shared.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Base Data Transfer Object class with common fields
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class BaseDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Entity ID
     */
    private Long id;
    
    /**
     * Creation timestamp
     */
    private LocalDateTime createdAt;
    
    /**
     * Last update timestamp
     */
    private LocalDateTime updatedAt;
    
    /**
     * Creator
     */
    private String createdBy;
    
    /**
     * Last modifier
     */
    private String updatedBy;
    
    /**
     * Deletion status
     */
    private Boolean isDeleted = false;
    
    /**
     * Version for optimistic locking
     */
    private Long version;
}