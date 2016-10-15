package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Test3App;

import com.mycompany.myapp.domain.Customer;
import com.mycompany.myapp.repository.CustomerRepository;
import com.mycompany.myapp.repository.search.CustomerSearchRepository;

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
 * Test class for the CustomerResource REST controller.
 *
 * @see CustomerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Test3App.class)
public class CustomerResourceIntTest {

    private static final String DEFAULT_FIRST_LINE_OF_ADDRESS = "A";
    private static final String UPDATED_FIRST_LINE_OF_ADDRESS = "B";
    private static final String DEFAULT_SECOND_LINE_OF_ADDRESS = "AA";
    private static final String UPDATED_SECOND_LINE_OF_ADDRESS = "BB";
    private static final String DEFAULT_CONTACT_NUMBER = "AAAAAAAAAAA";
    private static final String UPDATED_CONTACT_NUMBER = "BBBBBBBBBBB";
    private static final String DEFAULT_NAME = "AA";
    private static final String UPDATED_NAME = "BB";

    @Inject
    private CustomerRepository customerRepository;

    @Inject
    private CustomerSearchRepository customerSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCustomerMockMvc;

    private Customer customer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CustomerResource customerResource = new CustomerResource();
        ReflectionTestUtils.setField(customerResource, "customerSearchRepository", customerSearchRepository);
        ReflectionTestUtils.setField(customerResource, "customerRepository", customerRepository);
        this.restCustomerMockMvc = MockMvcBuilders.standaloneSetup(customerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createEntity(EntityManager em) {
        Customer customer = new Customer()
                .firstLineOfAddress(DEFAULT_FIRST_LINE_OF_ADDRESS)
                .secondLineOfAddress(DEFAULT_SECOND_LINE_OF_ADDRESS)
                .contactNumber(DEFAULT_CONTACT_NUMBER)
                .name(DEFAULT_NAME);
        return customer;
    }

    @Before
    public void initTest() {
        customerSearchRepository.deleteAll();
        customer = createEntity(em);
    }

    @Test
    @Transactional
    public void createCustomer() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // Create the Customer

        restCustomerMockMvc.perform(post("/api/customers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customer)))
                .andExpect(status().isCreated());

        // Validate the Customer in the database
        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).hasSize(databaseSizeBeforeCreate + 1);
        Customer testCustomer = customers.get(customers.size() - 1);
        assertThat(testCustomer.getFirstLineOfAddress()).isEqualTo(DEFAULT_FIRST_LINE_OF_ADDRESS);
        assertThat(testCustomer.getSecondLineOfAddress()).isEqualTo(DEFAULT_SECOND_LINE_OF_ADDRESS);
        assertThat(testCustomer.getContactNumber()).isEqualTo(DEFAULT_CONTACT_NUMBER);
        assertThat(testCustomer.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the Customer in ElasticSearch
        Customer customerEs = customerSearchRepository.findOne(testCustomer.getId());
        assertThat(customerEs).isEqualToComparingFieldByField(testCustomer);
    }

    @Test
    @Transactional
    public void getAllCustomers() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customers
        restCustomerMockMvc.perform(get("/api/customers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
                .andExpect(jsonPath("$.[*].firstLineOfAddress").value(hasItem(DEFAULT_FIRST_LINE_OF_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].secondLineOfAddress").value(hasItem(DEFAULT_SECOND_LINE_OF_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].contactNumber").value(hasItem(DEFAULT_CONTACT_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get the customer
        restCustomerMockMvc.perform(get("/api/customers/{id}", customer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(customer.getId().intValue()))
            .andExpect(jsonPath("$.firstLineOfAddress").value(DEFAULT_FIRST_LINE_OF_ADDRESS.toString()))
            .andExpect(jsonPath("$.secondLineOfAddress").value(DEFAULT_SECOND_LINE_OF_ADDRESS.toString()))
            .andExpect(jsonPath("$.contactNumber").value(DEFAULT_CONTACT_NUMBER.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCustomer() throws Exception {
        // Get the customer
        restCustomerMockMvc.perform(get("/api/customers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);
        customerSearchRepository.save(customer);
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer
        Customer updatedCustomer = customerRepository.findOne(customer.getId());
        updatedCustomer
                .firstLineOfAddress(UPDATED_FIRST_LINE_OF_ADDRESS)
                .secondLineOfAddress(UPDATED_SECOND_LINE_OF_ADDRESS)
                .contactNumber(UPDATED_CONTACT_NUMBER)
                .name(UPDATED_NAME);

        restCustomerMockMvc.perform(put("/api/customers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCustomer)))
                .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customers.get(customers.size() - 1);
        assertThat(testCustomer.getFirstLineOfAddress()).isEqualTo(UPDATED_FIRST_LINE_OF_ADDRESS);
        assertThat(testCustomer.getSecondLineOfAddress()).isEqualTo(UPDATED_SECOND_LINE_OF_ADDRESS);
        assertThat(testCustomer.getContactNumber()).isEqualTo(UPDATED_CONTACT_NUMBER);
        assertThat(testCustomer.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Customer in ElasticSearch
        Customer customerEs = customerSearchRepository.findOne(testCustomer.getId());
        assertThat(customerEs).isEqualToComparingFieldByField(testCustomer);
    }

    @Test
    @Transactional
    public void deleteCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);
        customerSearchRepository.save(customer);
        int databaseSizeBeforeDelete = customerRepository.findAll().size();

        // Get the customer
        restCustomerMockMvc.perform(delete("/api/customers/{id}", customer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean customerExistsInEs = customerSearchRepository.exists(customer.getId());
        assertThat(customerExistsInEs).isFalse();

        // Validate the database is empty
        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);
        customerSearchRepository.save(customer);

        // Search the customer
        restCustomerMockMvc.perform(get("/api/_search/customers?query=id:" + customer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstLineOfAddress").value(hasItem(DEFAULT_FIRST_LINE_OF_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].secondLineOfAddress").value(hasItem(DEFAULT_SECOND_LINE_OF_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].contactNumber").value(hasItem(DEFAULT_CONTACT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
}
