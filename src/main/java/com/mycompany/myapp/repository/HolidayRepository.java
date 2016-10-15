package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Holiday;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Holiday entity.
 */
@SuppressWarnings("unused")
public interface HolidayRepository extends JpaRepository<Holiday,Long> {

    @Query("select distinct holiday from Holiday holiday left join fetch holiday.employees")
    List<Holiday> findAllWithEagerRelationships();

    @Query("select holiday from Holiday holiday left join fetch holiday.employees where holiday.id =:id")
    Holiday findOneWithEagerRelationships(@Param("id") Long id);

}
