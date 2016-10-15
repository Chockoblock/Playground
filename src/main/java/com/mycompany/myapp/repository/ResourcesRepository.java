package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Resources;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Resources entity.
 */
@SuppressWarnings("unused")
public interface ResourcesRepository extends JpaRepository<Resources,Long> {

}
