package cn.cccs7.no_oops.api.dto;

import cn.cccs7.shared.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Book DTO for API communication
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BookDTO extends BaseDTO {
    
    private Long id;
    
    private String title;
    
    private String author;
    
    private String isbn;
    
    private String description;
}