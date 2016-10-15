package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Merchant;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Merchant entity.
 */
public interface MerchantSearchRepository extends ElasticsearchRepository<Merchant, Long> {
}
