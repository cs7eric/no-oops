package cn.cccs7.shared.repository;

import cn.cccs7.shared.po.BasePO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Base repository interface with common CRUD operations for persistence objects
 * @param <T> persistence object type
 * @param <ID> entity ID type
 */
@NoRepositoryBean
public interface BaseRepository<T extends BasePO, ID extends Serializable>
        extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
    
    /**
     * Find PO by ID and ensure it's not deleted
     *
     * @param id entity ID
     * @return optional PO
     */
    Optional<T> findByIdAndIsDeletedFalse(ID id);
    
    /**
     * Find all POs that are not deleted
     *
     * @return list of POs
     */
    List<T> findAllByIsDeletedFalse();
    
    /**
     * Find all POs that are not deleted with pagination
     *
     * @param pageable pagination information
     * @return page of POs
     */
    Page<T> findAllByIsDeletedFalse(Pageable pageable);
    
    /**
     * Find all POs that are not deleted matching the given specification
     *
     * @param spec specification to match
     * @return list of POs
     */
    List<T> findAllByIsDeletedFalse(Specification<T> spec);
    
    /**
     * Find all POs that are not deleted matching the given specification with pagination
     *
     * @param spec specification to match
     * @param pageable pagination information
     * @return page of POs
     */
    Page<T> findAllByIsDeletedFalse(Specification<T> spec, Pageable pageable);
    
    /**
     * Count all POs that are not deleted
     *
     * @return number of POs
     */
    long countByIsDeletedFalse();
    
    /**
     * Count POs that are not deleted matching the given specification
     *
     * @param spec specification to match
     * @return number of POs
     */
    long countByIsDeletedFalse(Specification<T> spec);
    
    /**
     * Check if PO exists by ID and is not deleted
     *
     * @param id entity ID
     * @return true if exists, false otherwise
     */
    boolean existsByIdAndIsDeletedFalse(ID id);
}