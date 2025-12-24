package cn.cccs7.autoconfig.config;

import cn.cccs7.autoconfig.entity.TestEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {JpaAuditingAutoConfiguration.class})
@Transactional
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/schema.sql")
public class JpaAuditingConfigTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JpaAuditingAutoConfiguration jpaAuditingAutoConfiguration;

    @Test
    public void testAuditingFieldsAreSetOnCreate() {
        // 设置当前认证用户
        User user = new User("testuser", "password", AuthorityUtils.createAuthorityList("ROLE_USER"));
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, "password", user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 创建测试实体
        TestEntity entity = new TestEntity();
        entity.setName("Test Name");
        entity.setDescription("Test Description");

        // 保存实体
        LocalDateTime beforeSave = LocalDateTime.now();
        entityManager.persist(entity);
        entityManager.flush();
        LocalDateTime afterSave = LocalDateTime.now();

        // 验证审计字段是否已正确设置
        assertThat(entity.getCreatedBy()).isEqualTo("testuser");
        assertThat(entity.getUpdatedBy()).isEqualTo("testuser");
        assertThat(entity.getCreatedAt()).isBetween(beforeSave, afterSave);
        assertThat(entity.getUpdatedAt()).isBetween(beforeSave, afterSave);
        assertThat(entity.getIsDeleted()).isEqualTo(false);
    }

    @Test
    public void testUpdatedByIsSetOnUpdate() {
        // 设置当前认证用户
        User user = new User("testuser", "password", AuthorityUtils.createAuthorityList("ROLE_USER"));
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, "password", user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 创建测试实体
        TestEntity entity = new TestEntity();
        entity.setName("Test Name");
        entity.setDescription("Test Description");

        // 保存实体
        entityManager.persist(entity);
        entityManager.flush();

        // 清除认证信息并设置新用户
        SecurityContextHolder.clearContext();
        User newUser = new User("newuser", "password", AuthorityUtils.createAuthorityList("ROLE_USER"));
        UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(newUser, "password", newUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);

        // 更新实体
        LocalDateTime beforeUpdate = LocalDateTime.now();
        entity.setName("Updated Name");
        entityManager.merge(entity);
        entityManager.flush();
        LocalDateTime afterUpdate = LocalDateTime.now();

        // 验证更新审计字段是否已正确设置
        assertThat(entity.getCreatedBy()).isEqualTo("testuser"); // 应该保持不变
        assertThat(entity.getUpdatedBy()).isEqualTo("newuser"); // 应该更新为新用户
        assertThat(entity.getUpdatedAt()).isBetween(beforeUpdate, afterUpdate);
    }
}