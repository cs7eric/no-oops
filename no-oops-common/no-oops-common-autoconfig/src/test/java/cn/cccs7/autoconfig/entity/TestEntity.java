package cn.cccs7.autoconfig.entity;

import cn.cccs7.shared.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * Test entity for autoconfiguration testing
 */
@Entity
@Getter
@Setter
public class TestEntity extends BaseEntity {
    
    private String name;
    
    private String description;
}