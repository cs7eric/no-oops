package cn.cccs7.controller;

import cn.cccs7.shared.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Book实体测试类，用于验证BaseEntity的审计功能
 */
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "logging.level.org.springframework.web=DEBUG"
})
class BookEntityTest {

    @Test
    void testBookEntityInheritance() {
        TestBook book = new TestBook();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setIsbn("1234567890");
        book.setDescription("Test Description");

        // Initially, audit fields should be null
        assertNull(book.getId());
        assertNull(book.getCreatedAt());
        assertNull(book.getUpdatedAt());
        assertNull(book.getCreatedBy());
        assertNull(book.getUpdatedBy());
        assertFalse(book.isDeleted());

        // Simulate creation
        book.setCreatedAt(LocalDateTime.now());
        book.setCreatedBy("test-user");

        // Simulate update
        book.setUpdatedAt(LocalDateTime.now());
        book.setUpdatedBy("test-user");

        // Test soft delete
        book.delete();
        assertTrue(book.isDeleted());

        // Verify all fields are properly set
        assertNotNull(book.getId());
        assertNotNull(book.getCreatedAt());
        assertNotNull(book.getUpdatedAt());
        assertEquals("test-user", book.getCreatedBy());
        assertEquals("test-user", book.getUpdatedBy());
    }

    @Entity
    @Getter
    @Setter
    static class TestBook extends BaseEntity {
        private String title;
        private String author;
        private String isbn;
        private String description;
    }
}