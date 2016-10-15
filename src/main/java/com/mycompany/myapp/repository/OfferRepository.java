package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Offer;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Offer entity.
 */
@SuppressWarnings("unused")
public interface OfferRepository extends JpaRepository<Offer,Long> {

}
