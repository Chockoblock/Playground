package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Resources;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Resources entity.
 */
public interface ResourcesSearchRepository extends ElasticsearchRepository<Resources, Long> {
}
