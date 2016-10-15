package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Test3App;

import com.mycompany.myapp.domain.Offer;
import com.mycompany.myapp.repository.OfferRepository;
import com.mycompany.myapp.repository.search.OfferSearchRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the OfferResource REST controller.
 *
 * @see OfferResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Test3App.class)
public class OfferResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final Integer DEFAULT_DISCOUNT = 0;
    private static final Integer UPDATED_DISCOUNT = 1;

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_START_DATE_STR = dateTimeFormatter.format(DEFAULT_START_DATE);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_END_DATE_STR = dateTimeFormatter.format(DEFAULT_END_DATE);

    @Inject
    private OfferRepository offerRepository;

    @Inject
    private OfferSearchRepository offerSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restOfferMockMvc;

    private Offer offer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OfferResource offerResource = new OfferResource();
        ReflectionTestUtils.setField(offerResource, "offerSearchRepository", offerSearchRepository);
        ReflectionTestUtils.setField(offerResource, "offerRepository", offerRepository);
        this.restOfferMockMvc = MockMvcBuilders.standaloneSetup(offerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Offer createEntity(EntityManager em) {
        Offer offer = new Offer()
                .discount(DEFAULT_DISCOUNT)
                .startDate(DEFAULT_START_DATE)
                .endDate(DEFAULT_END_DATE);
        return offer;
    }

    @Before
    public void initTest() {
        offerSearchRepository.deleteAll();
        offer = createEntity(em);
    }

    @Test
    @Transactional
    public void createOffer() throws Exception {
        int databaseSizeBeforeCreate = offerRepository.findAll().size();

        // Create the Offer

        restOfferMockMvc.perform(post("/api/offers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(offer)))
                .andExpect(status().isCreated());

        // Validate the Offer in the database
        List<Offer> offers = offerRepository.findAll();
        assertThat(offers).hasSize(databaseSizeBeforeCreate + 1);
        Offer testOffer = offers.get(offers.size() - 1);
        assertThat(testOffer.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testOffer.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testOffer.getEndDate()).isEqualTo(DEFAULT_END_DATE);

        // Validate the Offer in ElasticSearch
        Offer offerEs = offerSearchRepository.findOne(testOffer.getId());
        assertThat(offerEs).isEqualToComparingFieldByField(testOffer);
    }

    @Test
    @Transactional
    public void checkDiscountIsRequired() throws Exception {
        int databaseSizeBeforeTest = offerRepository.findAll().size();
        // set the field null
        offer.setDiscount(null);

        // Create the Offer, which fails.

        restOfferMockMvc.perform(post("/api/offers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(offer)))
                .andExpect(status().isBadRequest());

        List<Offer> offers = offerRepository.findAll();
        assertThat(offers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = offerRepository.findAll().size();
        // set the field null
        offer.setStartDate(null);

        // Create the Offer, which fails.

        restOfferMockMvc.perform(post("/api/offers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(offer)))
                .andExpect(status().isBadRequest());

        List<Offer> offers = offerRepository.findAll();
        assertThat(offers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = offerRepository.findAll().size();
        // set the field null
        offer.setEndDate(null);

        // Create the Offer, which fails.

        restOfferMockMvc.perform(post("/api/offers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(offer)))
                .andExpect(status().isBadRequest());

        List<Offer> offers = offerRepository.findAll();
        assertThat(offers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOffers() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offers
        restOfferMockMvc.perform(get("/api/offers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(offer.getId().intValue())))
                .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT)))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE_STR)))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE_STR)));
    }

    @Test
    @Transactional
    public void getOffer() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get the offer
        restOfferMockMvc.perform(get("/api/offers/{id}", offer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(offer.getId().intValue()))
            .andExpect(jsonPath("$.discount").value(DEFAULT_DISCOUNT))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE_STR))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingOffer() throws Exception {
        // Get the offer
        restOfferMockMvc.perform(get("/api/offers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOffer() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);
        offerSearchRepository.save(offer);
        int databaseSizeBeforeUpdate = offerRepository.findAll().size();

        // Update the offer
        Offer updatedOffer = offerRepository.findOne(offer.getId());
        updatedOffer
                .discount(UPDATED_DISCOUNT)
                .startDate(UPDATED_START_DATE)
                .endDate(UPDATED_END_DATE);

        restOfferMockMvc.perform(put("/api/offers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedOffer)))
                .andExpect(status().isOk());

        // Validate the Offer in the database
        List<Offer> offers = offerRepository.findAll();
        assertThat(offers).hasSize(databaseSizeBeforeUpdate);
        Offer testOffer = offers.get(offers.size() - 1);
        assertThat(testOffer.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testOffer.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testOffer.getEndDate()).isEqualTo(UPDATED_END_DATE);

        // Validate the Offer in ElasticSearch
        Offer offerEs = offerSearchRepository.findOne(testOffer.getId());
        assertThat(offerEs).isEqualToComparingFieldByField(testOffer);
    }

    @Test
    @Transactional
    public void deleteOffer() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);
        offerSearchRepository.save(offer);
        int databaseSizeBeforeDelete = offerRepository.findAll().size();

        // Get the offer
        restOfferMockMvc.perform(delete("/api/offers/{id}", offer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean offerExistsInEs = offerSearchRepository.exists(offer.getId());
        assertThat(offerExistsInEs).isFalse();

        // Validate the database is empty
        List<Offer> offers = offerRepository.findAll();
        assertThat(offers).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchOffer() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);
        offerSearchRepository.save(offer);

        // Search the offer
        restOfferMockMvc.perform(get("/api/_search/offers?query=id:" + offer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(offer.getId().intValue())))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE_STR)))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE_STR)));
    }
}
