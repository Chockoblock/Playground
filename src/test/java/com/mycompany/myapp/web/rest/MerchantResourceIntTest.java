package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Test3App;

import com.mycompany.myapp.domain.Merchant;
import com.mycompany.myapp.repository.MerchantRepository;
import com.mycompany.myapp.repository.search.MerchantSearchRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MerchantResource REST controller.
 *
 * @see MerchantResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Test3App.class)
public class MerchantResourceIntTest {

    private static final String DEFAULT_NAME = "AA";
    private static final String UPDATED_NAME = "BB";
    private static final String DEFAULT_LINE_ONE_OF_ADDRESS = "AA";
    private static final String UPDATED_LINE_ONE_OF_ADDRESS = "BB";
    private static final String DEFAULT_SECOND_LINE_OF_ADDRESS = "AA";
    private static final String UPDATED_SECOND_LINE_OF_ADDRESS = "BB";
    private static final String DEFAULT_POSTCODE = "AA";
    private static final String UPDATED_POSTCODE = "BB";
    private static final String DEFAULT_DESCRIPTION = "AA";
    private static final String UPDATED_DESCRIPTION = "BB";

    @Inject
    private MerchantRepository merchantRepository;

    @Inject
    private MerchantSearchRepository merchantSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restMerchantMockMvc;

    private Merchant merchant;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MerchantResource merchantResource = new MerchantResource();
        ReflectionTestUtils.setField(merchantResource, "merchantSearchRepository", merchantSearchRepository);
        ReflectionTestUtils.setField(merchantResource, "merchantRepository", merchantRepository);
        this.restMerchantMockMvc = MockMvcBuilders.standaloneSetup(merchantResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Merchant createEntity(EntityManager em) {
        Merchant merchant = new Merchant()
                .name(DEFAULT_NAME)
                .lineOneOfAddress(DEFAULT_LINE_ONE_OF_ADDRESS)
                .secondLineOfAddress(DEFAULT_SECOND_LINE_OF_ADDRESS)
                .postcode(DEFAULT_POSTCODE)
                .description(DEFAULT_DESCRIPTION);
        return merchant;
    }

    @Before
    public void initTest() {
        merchantSearchRepository.deleteAll();
        merchant = createEntity(em);
    }

    @Test
    @Transactional
    public void createMerchant() throws Exception {
        int databaseSizeBeforeCreate = merchantRepository.findAll().size();

        // Create the Merchant

        restMerchantMockMvc.perform(post("/api/merchants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(merchant)))
                .andExpect(status().isCreated());

        // Validate the Merchant in the database
        List<Merchant> merchants = merchantRepository.findAll();
        assertThat(merchants).hasSize(databaseSizeBeforeCreate + 1);
        Merchant testMerchant = merchants.get(merchants.size() - 1);
        assertThat(testMerchant.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMerchant.getLineOneOfAddress()).isEqualTo(DEFAULT_LINE_ONE_OF_ADDRESS);
        assertThat(testMerchant.getSecondLineOfAddress()).isEqualTo(DEFAULT_SECOND_LINE_OF_ADDRESS);
        assertThat(testMerchant.getPostcode()).isEqualTo(DEFAULT_POSTCODE);
        assertThat(testMerchant.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Merchant in ElasticSearch
        Merchant merchantEs = merchantSearchRepository.findOne(testMerchant.getId());
        assertThat(merchantEs).isEqualToComparingFieldByField(testMerchant);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = merchantRepository.findAll().size();
        // set the field null
        merchant.setName(null);

        // Create the Merchant, which fails.

        restMerchantMockMvc.perform(post("/api/merchants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(merchant)))
                .andExpect(status().isBadRequest());

        List<Merchant> merchants = merchantRepository.findAll();
        assertThat(merchants).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLineOneOfAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = merchantRepository.findAll().size();
        // set the field null
        merchant.setLineOneOfAddress(null);

        // Create the Merchant, which fails.

        restMerchantMockMvc.perform(post("/api/merchants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(merchant)))
                .andExpect(status().isBadRequest());

        List<Merchant> merchants = merchantRepository.findAll();
        assertThat(merchants).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSecondLineOfAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = merchantRepository.findAll().size();
        // set the field null
        merchant.setSecondLineOfAddress(null);

        // Create the Merchant, which fails.

        restMerchantMockMvc.perform(post("/api/merchants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(merchant)))
                .andExpect(status().isBadRequest());

        List<Merchant> merchants = merchantRepository.findAll();
        assertThat(merchants).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPostcodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = merchantRepository.findAll().size();
        // set the field null
        merchant.setPostcode(null);

        // Create the Merchant, which fails.

        restMerchantMockMvc.perform(post("/api/merchants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(merchant)))
                .andExpect(status().isBadRequest());

        List<Merchant> merchants = merchantRepository.findAll();
        assertThat(merchants).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = merchantRepository.findAll().size();
        // set the field null
        merchant.setDescription(null);

        // Create the Merchant, which fails.

        restMerchantMockMvc.perform(post("/api/merchants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(merchant)))
                .andExpect(status().isBadRequest());

        List<Merchant> merchants = merchantRepository.findAll();
        assertThat(merchants).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMerchants() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchants
        restMerchantMockMvc.perform(get("/api/merchants?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(merchant.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].lineOneOfAddress").value(hasItem(DEFAULT_LINE_ONE_OF_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].secondLineOfAddress").value(hasItem(DEFAULT_SECOND_LINE_OF_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].postcode").value(hasItem(DEFAULT_POSTCODE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getMerchant() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get the merchant
        restMerchantMockMvc.perform(get("/api/merchants/{id}", merchant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(merchant.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.lineOneOfAddress").value(DEFAULT_LINE_ONE_OF_ADDRESS.toString()))
            .andExpect(jsonPath("$.secondLineOfAddress").value(DEFAULT_SECOND_LINE_OF_ADDRESS.toString()))
            .andExpect(jsonPath("$.postcode").value(DEFAULT_POSTCODE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMerchant() throws Exception {
        // Get the merchant
        restMerchantMockMvc.perform(get("/api/merchants/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMerchant() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);
        merchantSearchRepository.save(merchant);
        int databaseSizeBeforeUpdate = merchantRepository.findAll().size();

        // Update the merchant
        Merchant updatedMerchant = merchantRepository.findOne(merchant.getId());
        updatedMerchant
                .name(UPDATED_NAME)
                .lineOneOfAddress(UPDATED_LINE_ONE_OF_ADDRESS)
                .secondLineOfAddress(UPDATED_SECOND_LINE_OF_ADDRESS)
                .postcode(UPDATED_POSTCODE)
                .description(UPDATED_DESCRIPTION);

        restMerchantMockMvc.perform(put("/api/merchants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMerchant)))
                .andExpect(status().isOk());

        // Validate the Merchant in the database
        List<Merchant> merchants = merchantRepository.findAll();
        assertThat(merchants).hasSize(databaseSizeBeforeUpdate);
        Merchant testMerchant = merchants.get(merchants.size() - 1);
        assertThat(testMerchant.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMerchant.getLineOneOfAddress()).isEqualTo(UPDATED_LINE_ONE_OF_ADDRESS);
        assertThat(testMerchant.getSecondLineOfAddress()).isEqualTo(UPDATED_SECOND_LINE_OF_ADDRESS);
        assertThat(testMerchant.getPostcode()).isEqualTo(UPDATED_POSTCODE);
        assertThat(testMerchant.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Merchant in ElasticSearch
        Merchant merchantEs = merchantSearchRepository.findOne(testMerchant.getId());
        assertThat(merchantEs).isEqualToComparingFieldByField(testMerchant);
    }

    @Test
    @Transactional
    public void deleteMerchant() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);
        merchantSearchRepository.save(merchant);
        int databaseSizeBeforeDelete = merchantRepository.findAll().size();

        // Get the merchant
        restMerchantMockMvc.perform(delete("/api/merchants/{id}", merchant.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean merchantExistsInEs = merchantSearchRepository.exists(merchant.getId());
        assertThat(merchantExistsInEs).isFalse();

        // Validate the database is empty
        List<Merchant> merchants = merchantRepository.findAll();
        assertThat(merchants).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMerchant() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);
        merchantSearchRepository.save(merchant);

        // Search the merchant
        restMerchantMockMvc.perform(get("/api/_search/merchants?query=id:" + merchant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(merchant.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].lineOneOfAddress").value(hasItem(DEFAULT_LINE_ONE_OF_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].secondLineOfAddress").value(hasItem(DEFAULT_SECOND_LINE_OF_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].postcode").value(hasItem(DEFAULT_POSTCODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
