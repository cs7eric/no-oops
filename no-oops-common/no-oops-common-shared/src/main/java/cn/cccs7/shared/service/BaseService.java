package cn.cccs7.shared.service;

import cn.cccs7.shared.entity.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Base service interface with common CRUD operations
 * @param <T> entity type
 * @param <ID> entity ID type
 */
public interface BaseService<T extends BaseEntity, ID extends Serializable> {
    
    /**
     * Save entity
     *
     * @param entity entity to save
     * @return saved entity
     */
    T save(T entity);
    
    /**
     * Save all entities
     *
     * @param entities entities to save
     * @return saved entities
     */
    List<T> saveAll(Iterable<T> entities);
    
    /**
     * Find entity by ID
     *
     * @param id entity ID
     * @return optional entity
     */
    Optional<T> findById(ID id);
    
    /**
     * Find all entities
     *
     * @return list of entities
     */
    List<T> findAll();
    
    /**
     * Find all entities with pagination
     *
     * @param pageable pagination information
     * @return page of entities
     */
    Page<T> findAll(Pageable pageable);
    
    /**
     * Check if entity exists by ID
     *
     * @param id entity ID
     * @return true if exists, false otherwise
     */
    boolean existsById(ID id);
    
    /**
     * Delete entity by ID
     *
     * @param id entity ID
     */
    void deleteById(ID id);
    
    /**
     * Delete entity
     *
     * @param entity entity to delete
     */
    void delete(T entity);
    
    /**
     * Delete all entities
     */
    void deleteAll();
}