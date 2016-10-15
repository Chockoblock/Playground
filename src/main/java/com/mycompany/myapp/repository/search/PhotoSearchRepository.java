package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Photo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Photo entity.
 */
public interface PhotoSearchRepository extends ElasticsearchRepository<Photo, Long> {
}
