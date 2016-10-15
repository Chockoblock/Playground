package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.BookingItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the BookingItem entity.
 */
public interface BookingItemSearchRepository extends ElasticsearchRepository<BookingItem, Long> {
}
