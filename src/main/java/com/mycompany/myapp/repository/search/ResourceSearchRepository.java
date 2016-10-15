package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Resource;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Resource entity.
 */
public interface ResourceSearchRepository extends ElasticsearchRepository<Resource, Long> {
}
