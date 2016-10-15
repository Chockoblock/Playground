package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Merchant;

import com.mycompany.myapp.repository.MerchantRepository;
import com.mycompany.myapp.repository.search.MerchantSearchRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Merchant.
 */
@RestController
@RequestMapping("/api")
public class MerchantResource {

    private final Logger log = LoggerFactory.getLogger(MerchantResource.class);
        
    @Inject
    private MerchantRepository merchantRepository;

    @Inject
    private MerchantSearchRepository merchantSearchRepository;

    /**
     * POST  /merchants : Create a new merchant.
     *
     * @param merchant the merchant to create
     * @return the ResponseEntity with status 201 (Created) and with body the new merchant, or with status 400 (Bad Request) if the merchant has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/merchants",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Merchant> createMerchant(@Valid @RequestBody Merchant merchant) throws URISyntaxException {
        log.debug("REST request to save Merchant : {}", merchant);
        if (merchant.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("merchant", "idexists", "A new merchant cannot already have an ID")).body(null);
        }
        Merchant result = merchantRepository.save(merchant);
        merchantSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/merchants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("merchant", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /merchants : Updates an existing merchant.
     *
     * @param merchant the merchant to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated merchant,
     * or with status 400 (Bad Request) if the merchant is not valid,
     * or with status 500 (Internal Server Error) if the merchant couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/merchants",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Merchant> updateMerchant(@Valid @RequestBody Merchant merchant) throws URISyntaxException {
        log.debug("REST request to update Merchant : {}", merchant);
        if (merchant.getId() == null) {
            return createMerchant(merchant);
        }
        Merchant result = merchantRepository.save(merchant);
        merchantSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("merchant", merchant.getId().toString()))
            .body(result);
    }

    /**
     * GET  /merchants : get all the merchants.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of merchants in body
     */
    @RequestMapping(value = "/merchants",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Merchant> getAllMerchants() {
        log.debug("REST request to get all Merchants");
        List<Merchant> merchants = merchantRepository.findAll();
        return merchants;
    }

    /**
     * GET  /merchants/:id : get the "id" merchant.
     *
     * @param id the id of the merchant to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the merchant, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/merchants/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Merchant> getMerchant(@PathVariable Long id) {
        log.debug("REST request to get Merchant : {}", id);
        Merchant merchant = merchantRepository.findOne(id);
        return Optional.ofNullable(merchant)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /merchants/:id : delete the "id" merchant.
     *
     * @param id the id of the merchant to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/merchants/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMerchant(@PathVariable Long id) {
        log.debug("REST request to delete Merchant : {}", id);
        merchantRepository.delete(id);
        merchantSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("merchant", id.toString())).build();
    }

    /**
     * SEARCH  /_search/merchants?query=:query : search for the merchant corresponding
     * to the query.
     *
     * @param query the query of the merchant search 
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/merchants",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Merchant> searchMerchants(@RequestParam String query) {
        log.debug("REST request to search Merchants for query {}", query);
        return StreamSupport
            .stream(merchantSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
