package cn.cccs7.controller;

import cn.cccs7.shared.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BaseEntity功能测试类，用于验证BaseEntity的审计功能
 */
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "logging.level.org.springframework.web=DEBUG"
})
class BaseEntityTest {

    @Test
    void testBaseEntityAuditingFields() {
        TestEntity entity = new TestEntity();
        entity.setName("Test Entity");
        entity.setDescription("Test Description");

        // Verify initial state
        assertNull(entity.getId());
        assertNull(entity.getCreatedAt());
        assertNull(entity.getUpdatedAt());
        assertNull(entity.getCreatedBy());
        assertNull(entity.getUpdatedBy());
        assertFalse(entity.isDeleted());

        // Simulate creation
        entity.setCreatedAt(LocalDateTime.now());
        entity.setCreatedBy("test-user");

        // Simulate update
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy("test-user");

        // Test soft delete
        entity.delete();
        assertTrue(entity.isDeleted());

        // Verify all fields are properly set
        assertNotNull(entity.getId());
        assertNotNull(entity.getCreatedAt());
        assertNotNull(entity.getUpdatedAt());
        assertEquals("test-user", entity.getCreatedBy());
        assertEquals("test-user", entity.getUpdatedBy());
    }

    @Entity
    @Getter
    @Setter
    static class TestEntity extends BaseEntity {
        private String name;
        private String description;
    }
}