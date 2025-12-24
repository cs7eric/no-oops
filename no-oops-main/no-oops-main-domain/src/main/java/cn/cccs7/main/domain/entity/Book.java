package cn.cccs7.entity;

import cn.cccs7.shared.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Book entity
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Book extends BaseEntity {
    
    private String title;
    
    private String author;
    
    private String isbn;
    
    private String description;
}