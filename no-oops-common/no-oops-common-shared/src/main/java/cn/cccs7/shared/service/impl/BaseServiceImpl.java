package cn.cccs7.shared.service.impl;

import cn.cccs7.shared.po.BasePO;
import cn.cccs7.shared.repository.BaseRepository;
import cn.cccs7.shared.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Base service implementation with common CRUD operations for domain entities
 * @param <T> domain entity type
 * @param <ID> entity ID type
 * @param <R> repository type
 */
@RequiredArgsConstructor
public class BaseServiceImpl<T extends BasePO, ID extends Serializable, R extends BaseRepository<T, ID>>
        implements BaseService<T, ID> {
    
    private static final Logger log = LoggerFactory.getLogger(BaseServiceImpl.class);
    
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
        return repository.findByIdAndIsDeletedFalse(id);
    }
    
    @Override
    public List<T> findAll() {
        log.debug("Finding all entities");
        return repository.findAllByIsDeletedFalse();
    }
    
    @Override
    public Page<T> findAll(Pageable pageable) {
        log.debug("Finding all entities with pagination");
        return repository.findAllByIsDeletedFalse(pageable);
    }
    
    @Override
    public List<T> findAll(Specification<T> spec) {
        log.debug("Finding all entities with specification");
        return repository.findAllByIsDeletedFalse(spec);
    }
    
    @Override
    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        log.debug("Finding all entities with specification and pagination");
        return repository.findAllByIsDeletedFalse(spec, pageable);
    }
    
    @Override
    public long count() {
        log.debug("Counting all entities");
        return repository.countByIsDeletedFalse();
    }
    
    @Override
    public long count(Specification<T> spec) {
        log.debug("Counting entities with specification");
        return repository.countByIsDeletedFalse(spec);
    }
    
    @Override
    public boolean existsById(ID id) {
        log.debug("Checking if entity exists by ID: {}", id);
        return repository.existsByIdAndIsDeletedFalse(id);
    }
    
    @Override
    public void deleteById(ID id) {
        log.debug("Logically deleting entity by ID: {}", id);
        findById(id).ifPresent(entity -> {
            entity.delete();
            repository.save(entity);
        });
    }
    
    @Override
    public void delete(T entity) {
        log.debug("Logically deleting entity: {}", entity);
        entity.delete();
        repository.save(entity);
    }
    
    @Override
    public void deleteAll() {
        log.debug("Logically deleting all entities");
        List<T> entities = findAll();
        entities.forEach(BasePO::delete);
        repository.saveAll(entities);
    }
    
    @Override
    public void deleteByIdPermanently(ID id) {
        log.debug("Permanently deleting entity by ID: {}", id);
        repository.deleteById(id);
    }
    
    @Override
    public void deletePermanently(T entity) {
        log.debug("Permanently deleting entity: {}", entity);
        repository.delete(entity);
    }
    
    @Override
    public void deleteAllPermanently() {
        log.debug("Permanently deleting all entities");
        repository.deleteAll();
    }
}