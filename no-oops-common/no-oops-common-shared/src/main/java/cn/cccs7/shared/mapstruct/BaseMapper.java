package cn.cccs7.shared.mapstruct;

import org.mapstruct.IterableMapping;
import org.mapstruct.Named;

import java.util.List;

/**
 * Base mapper interface with common conversion methods
 * @param <S> source type
 * @param <T> target type
 */
public interface BaseMapper<S, T> {
    
    /**
     * Convert source object to target object
     *
     * @param source source object
     * @return target object
     */
    @Named("toTarget")
    T toTarget(S source);
    
    /**
     * Convert target object to source object
     *
     * @param target target object
     * @return source object
     */
    @Named("toSource")
    S toSource(T target);
    
    /**
     * Convert list of source objects to list of target objects
     *
     * @param sources list of source objects
     * @return list of target objects
     */
    @IterableMapping(qualifiedByName = "toTarget")
    List<T> toTargetList(List<S> sources);
    
    /**
     * Convert list of target objects to list of source objects
     *
     * @param targets list of target objects
     * @return list of source objects
     */
    @IterableMapping(qualifiedByName = "toSource")
    List<S> toSourceList(List<T> targets);
    
    /**
     * Convert source object to target object with additional processing
     *
     * @param source source object
     * @return target object
     */
    @Named("toTargetWithProcessing")
    default T toTargetWithProcessing(S source) {
        return toTarget(source);
    }
    
    /**
     * Convert target object to source object with additional processing
     *
     * @param target target object
     * @return source object
     */
    @Named("toSourceWithProcessing")
    default S toSourceWithProcessing(T target) {
        return toSource(target);
    }
}