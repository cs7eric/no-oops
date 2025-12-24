package cn.cccs7.main.infra.entity;

import cn.cccs7.shared.po.BasePO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Book Persistence Object
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "book")
public class BookPO extends BasePO {
    
    @Column(name = "title")
    private String title;
    
    @Column(name = "author")
    private String author;
    
    @Column(name = "isbn")
    private String isbn;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}