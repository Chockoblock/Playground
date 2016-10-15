package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Resource;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Resource entity.
 */
@SuppressWarnings("unused")
public interface ResourceRepository extends JpaRepository<Resource,Long> {

    @Query("select distinct resource from Resource resource left join fetch resource.employees")
    List<Resource> findAllWithEagerRelationships();

    @Query("select resource from Resource resource left join fetch resource.employees where resource.id =:id")
    Resource findOneWithEagerRelationships(@Param("id") Long id);

}
