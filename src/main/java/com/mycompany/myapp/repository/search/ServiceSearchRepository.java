package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Service;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Service entity.
 */
public interface ServiceSearchRepository extends ElasticsearchRepository<Service, Long> {
}
