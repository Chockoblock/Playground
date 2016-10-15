package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Holiday;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Holiday entity.
 */
public interface HolidaySearchRepository extends ElasticsearchRepository<Holiday, Long> {
}
