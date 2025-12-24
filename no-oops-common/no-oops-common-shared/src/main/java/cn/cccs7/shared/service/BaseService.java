package cn.cccs7.shared.service;

import cn.cccs7.shared.po.BasePO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Base service interface with common CRUD operations for domain entities
 * @param <T> domain entity type
 * @param <ID> entity ID type
 */
public interface BaseService<T extends BasePO, ID extends Serializable> {
    
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
     * Find all entities matching the given specification
     *
     * @param spec specification to match
     * @return list of entities
     */
    List<T> findAll(Specification<T> spec);
    
    /**
     * Find all entities matching the given specification with pagination
     *
     * @param spec specification to match
     * @param pageable pagination information
     * @return page of entities
     */
    Page<T> findAll(Specification<T> spec, Pageable pageable);
    
    /**
     * Count all entities
     *
     * @return number of entities
     */
    long count();
    
    /**
     * Count entities matching the given specification
     *
     * @param spec specification to match
     * @return number of entities
     */
    long count(Specification<T> spec);
    
    /**
     * Check if entity exists by ID
     *
     * @param id entity ID
     * @return true if exists, false otherwise
     */
    boolean existsById(ID id);
    
    /**
     * Delete entity by ID (logical delete)
     *
     * @param id entity ID
     */
    void deleteById(ID id);
    
    /**
     * Delete entity (logical delete)
     *
     * @param entity entity to delete
     */
    void delete(T entity);
    
    /**
     * Delete all entities (logical delete)
     */
    void deleteAll();
    
    /**
     * Permanently delete entity by ID
     *
     * @param id entity ID
     */
    void deleteByIdPermanently(ID id);
    
    /**
     * Permanently delete entity
     *
     * @param entity entity to delete
     */
    void deletePermanently(T entity);
    
    /**
     * Permanently delete all entities
     */
    void deleteAllPermanently();
}