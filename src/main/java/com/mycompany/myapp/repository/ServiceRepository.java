package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Service;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Service entity.
 */
@SuppressWarnings("unused")
public interface ServiceRepository extends JpaRepository<Service,Long> {

    @Query("select distinct service from Service service left join fetch service.holidays")
    List<Service> findAllWithEagerRelationships();

    @Query("select service from Service service left join fetch service.holidays where service.id =:id")
    Service findOneWithEagerRelationships(@Param("id") Long id);

}
