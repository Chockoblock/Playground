package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.BookingItem;

import com.mycompany.myapp.repository.BookingItemRepository;
import com.mycompany.myapp.repository.search.BookingItemSearchRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing BookingItem.
 */
@RestController
@RequestMapping("/api")
public class BookingItemResource {

    private final Logger log = LoggerFactory.getLogger(BookingItemResource.class);
        
    @Inject
    private BookingItemRepository bookingItemRepository;

    @Inject
    private BookingItemSearchRepository bookingItemSearchRepository;

    /**
     * POST  /booking-items : Create a new bookingItem.
     *
     * @param bookingItem the bookingItem to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bookingItem, or with status 400 (Bad Request) if the bookingItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/booking-items",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BookingItem> createBookingItem(@RequestBody BookingItem bookingItem) throws URISyntaxException {
        log.debug("REST request to save BookingItem : {}", bookingItem);
        if (bookingItem.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bookingItem", "idexists", "A new bookingItem cannot already have an ID")).body(null);
        }
        BookingItem result = bookingItemRepository.save(bookingItem);
        bookingItemSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/booking-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("bookingItem", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /booking-items : Updates an existing bookingItem.
     *
     * @param bookingItem the bookingItem to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bookingItem,
     * or with status 400 (Bad Request) if the bookingItem is not valid,
     * or with status 500 (Internal Server Error) if the bookingItem couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/booking-items",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BookingItem> updateBookingItem(@RequestBody BookingItem bookingItem) throws URISyntaxException {
        log.debug("REST request to update BookingItem : {}", bookingItem);
        if (bookingItem.getId() == null) {
            return createBookingItem(bookingItem);
        }
        BookingItem result = bookingItemRepository.save(bookingItem);
        bookingItemSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("bookingItem", bookingItem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /booking-items : get all the bookingItems.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of bookingItems in body
     */
    @RequestMapping(value = "/booking-items",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<BookingItem> getAllBookingItems() {
        log.debug("REST request to get all BookingItems");
        List<BookingItem> bookingItems = bookingItemRepository.findAll();
        return bookingItems;
    }

    /**
     * GET  /booking-items/:id : get the "id" bookingItem.
     *
     * @param id the id of the bookingItem to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bookingItem, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/booking-items/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BookingItem> getBookingItem(@PathVariable Long id) {
        log.debug("REST request to get BookingItem : {}", id);
        BookingItem bookingItem = bookingItemRepository.findOne(id);
        return Optional.ofNullable(bookingItem)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /booking-items/:id : delete the "id" bookingItem.
     *
     * @param id the id of the bookingItem to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/booking-items/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBookingItem(@PathVariable Long id) {
        log.debug("REST request to delete BookingItem : {}", id);
        bookingItemRepository.delete(id);
        bookingItemSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bookingItem", id.toString())).build();
    }

    /**
     * SEARCH  /_search/booking-items?query=:query : search for the bookingItem corresponding
     * to the query.
     *
     * @param query the query of the bookingItem search 
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/booking-items",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<BookingItem> searchBookingItems(@RequestParam String query) {
        log.debug("REST request to search BookingItems for query {}", query);
        return StreamSupport
            .stream(bookingItemSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
