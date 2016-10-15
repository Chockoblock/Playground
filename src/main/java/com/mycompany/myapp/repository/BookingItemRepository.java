package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BookingItem;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BookingItem entity.
 */
@SuppressWarnings("unused")
public interface BookingItemRepository extends JpaRepository<BookingItem,Long> {

}
