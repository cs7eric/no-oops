package cn.cccs7.shared.service.impl;

import cn.cccs7.shared.entity.BaseEntity;
import cn.cccs7.shared.repository.BaseRepository;
import cn.cccs7.shared.service.BaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Base service implementation with common CRUD operations
 * @param <T> entity type
 * @param <ID> entity ID type
 * @param <R> repository type
 */
@Slf4j
@RequiredArgsConstructor
public class BaseServiceImpl<T extends BaseEntity, ID extends Serializable, R extends BaseRepository<T, ID>> 
        implements BaseService<T, ID> {
    
    protected final R repository;
    
    @Override
    public T save(T entity) {
        log.debug("Saving entity: {}", entity);
        return repository.save(entity);
    }
    
    @Override
    public List<T> saveAll(Iterable<T> entities) {
        log.debug("Saving all entities");
        return repository.saveAll(entities);
    }
    
    @Override
    public Optional<T> findById(ID id) {
        log.debug("Finding entity by ID: {}", id);
        return repository.findByIdAndDeletedFalse(id);
    }
    
    @Override
    public List<T> findAll() {
        log.debug("Finding all entities");
        return repository.findAllByDeletedFalse();
    }
    
    @Override
    public Page<T> findAll(Pageable pageable) {
        log.debug("Finding all entities with pagination");
        return repository.findAll(pageable);
    }
    
    @Override
    public boolean existsById(ID id) {
        log.debug("Checking if entity exists by ID: {}", id);
        return repository.existsByIdAndDeletedFalse(id);
    }
    
    @Override
    public void deleteById(ID id) {
        log.debug("Deleting entity by ID: {}", id);
        findById(id).ifPresent(entity -> {
            entity.setDeleted(true);
            repository.save(entity);
        });
    }
    
    @Override
    public void delete(T entity) {
        log.debug("Deleting entity: {}", entity);
        entity.setDeleted(true);
        repository.save(entity);
    }
    
    @Override
    public void deleteAll() {
        log.debug("Deleting all entities");
        List<T> entities = findAll();
        entities.forEach(entity -> entity.setDeleted(true));
        repository.saveAll(entities);
    }
}