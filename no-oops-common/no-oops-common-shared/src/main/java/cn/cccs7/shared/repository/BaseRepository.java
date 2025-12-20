package cn.cccs7.shared.repository;

import cn.cccs7.shared.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Base repository interface with common CRUD operations
 * @param <T> entity type
 * @param <ID> entity ID type
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, ID extends Serializable> 
        extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
    
    /**
     * Find entity by ID and ensure it's not deleted
     *
     * @param id entity ID
     * @return optional entity
     */
    Optional<T> findByIdAndDeletedFalse(ID id);
    
    /**
     * Find all entities that are not deleted
     *
     * @return list of entities
     */
    List<T> findAllByDeletedFalse();
    
    /**
     * Check if entity exists by ID and is not deleted
     *
     * @param id entity ID
     * @return true if exists, false otherwise
     */
    boolean existsByIdAndDeletedFalse(ID id);
}