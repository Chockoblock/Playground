package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Test3App;

import com.mycompany.myapp.domain.BookingItem;
import com.mycompany.myapp.repository.BookingItemRepository;
import com.mycompany.myapp.repository.search.BookingItemSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BookingItemResource REST controller.
 *
 * @see BookingItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Test3App.class)
public class BookingItemResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);
    private static final String DEFAULT_SERVICE_TYPE = "AAAAA";
    private static final String UPDATED_SERVICE_TYPE = "BBBBB";

    private static final ZonedDateTime DEFAULT_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_START_TIME_STR = dateTimeFormatter.format(DEFAULT_START_TIME);

    private static final ZonedDateTime DEFAULT_END_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_END_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_END_TIME_STR = dateTimeFormatter.format(DEFAULT_END_TIME);

    @Inject
    private BookingItemRepository bookingItemRepository;

    @Inject
    private BookingItemSearchRepository bookingItemSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restBookingItemMockMvc;

    private BookingItem bookingItem;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BookingItemResource bookingItemResource = new BookingItemResource();
        ReflectionTestUtils.setField(bookingItemResource, "bookingItemSearchRepository", bookingItemSearchRepository);
        ReflectionTestUtils.setField(bookingItemResource, "bookingItemRepository", bookingItemRepository);
        this.restBookingItemMockMvc = MockMvcBuilders.standaloneSetup(bookingItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookingItem createEntity(EntityManager em) {
        BookingItem bookingItem = new BookingItem()
                .price(DEFAULT_PRICE)
                .serviceType(DEFAULT_SERVICE_TYPE)
                .startTime(DEFAULT_START_TIME)
                .endTime(DEFAULT_END_TIME);
        return bookingItem;
    }

    @Before
    public void initTest() {
        bookingItemSearchRepository.deleteAll();
        bookingItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createBookingItem() throws Exception {
        int databaseSizeBeforeCreate = bookingItemRepository.findAll().size();

        // Create the BookingItem

        restBookingItemMockMvc.perform(post("/api/booking-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookingItem)))
                .andExpect(status().isCreated());

        // Validate the BookingItem in the database
        List<BookingItem> bookingItems = bookingItemRepository.findAll();
        assertThat(bookingItems).hasSize(databaseSizeBeforeCreate + 1);
        BookingItem testBookingItem = bookingItems.get(bookingItems.size() - 1);
        assertThat(testBookingItem.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testBookingItem.getServiceType()).isEqualTo(DEFAULT_SERVICE_TYPE);
        assertThat(testBookingItem.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testBookingItem.getEndTime()).isEqualTo(DEFAULT_END_TIME);

        // Validate the BookingItem in ElasticSearch
        BookingItem bookingItemEs = bookingItemSearchRepository.findOne(testBookingItem.getId());
        assertThat(bookingItemEs).isEqualToComparingFieldByField(testBookingItem);
    }

    @Test
    @Transactional
    public void getAllBookingItems() throws Exception {
        // Initialize the database
        bookingItemRepository.saveAndFlush(bookingItem);

        // Get all the bookingItems
        restBookingItemMockMvc.perform(get("/api/booking-items?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(bookingItem.getId().intValue())))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
                .andExpect(jsonPath("$.[*].serviceType").value(hasItem(DEFAULT_SERVICE_TYPE.toString())))
                .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME_STR)))
                .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME_STR)));
    }

    @Test
    @Transactional
    public void getBookingItem() throws Exception {
        // Initialize the database
        bookingItemRepository.saveAndFlush(bookingItem);

        // Get the bookingItem
        restBookingItemMockMvc.perform(get("/api/booking-items/{id}", bookingItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bookingItem.getId().intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.serviceType").value(DEFAULT_SERVICE_TYPE.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME_STR))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingBookingItem() throws Exception {
        // Get the bookingItem
        restBookingItemMockMvc.perform(get("/api/booking-items/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBookingItem() throws Exception {
        // Initialize the database
        bookingItemRepository.saveAndFlush(bookingItem);
        bookingItemSearchRepository.save(bookingItem);
        int databaseSizeBeforeUpdate = bookingItemRepository.findAll().size();

        // Update the bookingItem
        BookingItem updatedBookingItem = bookingItemRepository.findOne(bookingItem.getId());
        updatedBookingItem
                .price(UPDATED_PRICE)
                .serviceType(UPDATED_SERVICE_TYPE)
                .startTime(UPDATED_START_TIME)
                .endTime(UPDATED_END_TIME);

        restBookingItemMockMvc.perform(put("/api/booking-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBookingItem)))
                .andExpect(status().isOk());

        // Validate the BookingItem in the database
        List<BookingItem> bookingItems = bookingItemRepository.findAll();
        assertThat(bookingItems).hasSize(databaseSizeBeforeUpdate);
        BookingItem testBookingItem = bookingItems.get(bookingItems.size() - 1);
        assertThat(testBookingItem.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testBookingItem.getServiceType()).isEqualTo(UPDATED_SERVICE_TYPE);
        assertThat(testBookingItem.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testBookingItem.getEndTime()).isEqualTo(UPDATED_END_TIME);

        // Validate the BookingItem in ElasticSearch
        BookingItem bookingItemEs = bookingItemSearchRepository.findOne(testBookingItem.getId());
        assertThat(bookingItemEs).isEqualToComparingFieldByField(testBookingItem);
    }

    @Test
    @Transactional
    public void deleteBookingItem() throws Exception {
        // Initialize the database
        bookingItemRepository.saveAndFlush(bookingItem);
        bookingItemSearchRepository.save(bookingItem);
        int databaseSizeBeforeDelete = bookingItemRepository.findAll().size();

        // Get the bookingItem
        restBookingItemMockMvc.perform(delete("/api/booking-items/{id}", bookingItem.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean bookingItemExistsInEs = bookingItemSearchRepository.exists(bookingItem.getId());
        assertThat(bookingItemExistsInEs).isFalse();

        // Validate the database is empty
        List<BookingItem> bookingItems = bookingItemRepository.findAll();
        assertThat(bookingItems).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBookingItem() throws Exception {
        // Initialize the database
        bookingItemRepository.saveAndFlush(bookingItem);
        bookingItemSearchRepository.save(bookingItem);

        // Search the bookingItem
        restBookingItemMockMvc.perform(get("/api/_search/booking-items?query=id:" + bookingItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookingItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].serviceType").value(hasItem(DEFAULT_SERVICE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME_STR)))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME_STR)));
    }
}
