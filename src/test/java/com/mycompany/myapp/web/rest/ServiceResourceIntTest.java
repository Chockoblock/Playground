package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Test3App;

import com.mycompany.myapp.domain.Service;
import com.mycompany.myapp.repository.ServiceRepository;
import com.mycompany.myapp.repository.search.ServiceSearchRepository;

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
 * Test class for the ServiceResource REST controller.
 *
 * @see ServiceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Test3App.class)
public class ServiceResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final Boolean DEFAULT_MONDAY_IS_OPEN = false;
    private static final Boolean UPDATED_MONDAY_IS_OPEN = true;

    private static final Boolean DEFAULT_TUESDAY_IS_OPEN = false;
    private static final Boolean UPDATED_TUESDAY_IS_OPEN = true;

    private static final Boolean DEFAULT_WEDNESDAY_IS_OPEN = false;
    private static final Boolean UPDATED_WEDNESDAY_IS_OPEN = true;

    private static final Boolean DEFAULT_THURSDAY_IS_OPEN = false;
    private static final Boolean UPDATED_THURSDAY_IS_OPEN = true;

    private static final Boolean DEFAULT_FRIDAY_IS_OPEN = false;
    private static final Boolean UPDATED_FRIDAY_IS_OPEN = true;

    private static final Boolean DEFAULT_SATURDAY_IS_OPEN = false;
    private static final Boolean UPDATED_SATURDAY_IS_OPEN = true;

    private static final Boolean DEFAULT_SUNDAY_IS_OPEN = false;
    private static final Boolean UPDATED_SUNDAY_IS_OPEN = true;
    private static final String DEFAULT_NAME = "AA";
    private static final String UPDATED_NAME = "BB";
    private static final String DEFAULT_DESCRIPTION = "AA";
    private static final String UPDATED_DESCRIPTION = "BB";
    private static final String DEFAULT_SERVICE_TYPE = "AA";
    private static final String UPDATED_SERVICE_TYPE = "BB";

    private static final ZonedDateTime DEFAULT_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_START_TIME_STR = dateTimeFormatter.format(DEFAULT_START_TIME);

    private static final ZonedDateTime DEFAULT_END_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_END_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_END_TIME_STR = dateTimeFormatter.format(DEFAULT_END_TIME);

    @Inject
    private ServiceRepository serviceRepository;

    @Inject
    private ServiceSearchRepository serviceSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restServiceMockMvc;

    private Service service;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ServiceResource serviceResource = new ServiceResource();
        ReflectionTestUtils.setField(serviceResource, "serviceSearchRepository", serviceSearchRepository);
        ReflectionTestUtils.setField(serviceResource, "serviceRepository", serviceRepository);
        this.restServiceMockMvc = MockMvcBuilders.standaloneSetup(serviceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Service createEntity(EntityManager em) {
        Service service = new Service()
                .mondayIsOpen(DEFAULT_MONDAY_IS_OPEN)
                .tuesdayIsOpen(DEFAULT_TUESDAY_IS_OPEN)
                .wednesdayIsOpen(DEFAULT_WEDNESDAY_IS_OPEN)
                .thursdayIsOpen(DEFAULT_THURSDAY_IS_OPEN)
                .fridayIsOpen(DEFAULT_FRIDAY_IS_OPEN)
                .saturdayIsOpen(DEFAULT_SATURDAY_IS_OPEN)
                .sundayIsOpen(DEFAULT_SUNDAY_IS_OPEN)
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .serviceType(DEFAULT_SERVICE_TYPE)
                .startTime(DEFAULT_START_TIME)
                .endTime(DEFAULT_END_TIME);
        return service;
    }

    @Before
    public void initTest() {
        serviceSearchRepository.deleteAll();
        service = createEntity(em);
    }

    @Test
    @Transactional
    public void createService() throws Exception {
        int databaseSizeBeforeCreate = serviceRepository.findAll().size();

        // Create the Service

        restServiceMockMvc.perform(post("/api/services")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(service)))
                .andExpect(status().isCreated());

        // Validate the Service in the database
        List<Service> services = serviceRepository.findAll();
        assertThat(services).hasSize(databaseSizeBeforeCreate + 1);
        Service testService = services.get(services.size() - 1);
        assertThat(testService.isMondayIsOpen()).isEqualTo(DEFAULT_MONDAY_IS_OPEN);
        assertThat(testService.isTuesdayIsOpen()).isEqualTo(DEFAULT_TUESDAY_IS_OPEN);
        assertThat(testService.isWednesdayIsOpen()).isEqualTo(DEFAULT_WEDNESDAY_IS_OPEN);
        assertThat(testService.isThursdayIsOpen()).isEqualTo(DEFAULT_THURSDAY_IS_OPEN);
        assertThat(testService.isFridayIsOpen()).isEqualTo(DEFAULT_FRIDAY_IS_OPEN);
        assertThat(testService.isSaturdayIsOpen()).isEqualTo(DEFAULT_SATURDAY_IS_OPEN);
        assertThat(testService.isSundayIsOpen()).isEqualTo(DEFAULT_SUNDAY_IS_OPEN);
        assertThat(testService.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testService.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testService.getServiceType()).isEqualTo(DEFAULT_SERVICE_TYPE);
        assertThat(testService.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testService.getEndTime()).isEqualTo(DEFAULT_END_TIME);

        // Validate the Service in ElasticSearch
        Service serviceEs = serviceSearchRepository.findOne(testService.getId());
        assertThat(serviceEs).isEqualToComparingFieldByField(testService);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceRepository.findAll().size();
        // set the field null
        service.setName(null);

        // Create the Service, which fails.

        restServiceMockMvc.perform(post("/api/services")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(service)))
                .andExpect(status().isBadRequest());

        List<Service> services = serviceRepository.findAll();
        assertThat(services).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceRepository.findAll().size();
        // set the field null
        service.setDescription(null);

        // Create the Service, which fails.

        restServiceMockMvc.perform(post("/api/services")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(service)))
                .andExpect(status().isBadRequest());

        List<Service> services = serviceRepository.findAll();
        assertThat(services).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkServiceTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceRepository.findAll().size();
        // set the field null
        service.setServiceType(null);

        // Create the Service, which fails.

        restServiceMockMvc.perform(post("/api/services")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(service)))
                .andExpect(status().isBadRequest());

        List<Service> services = serviceRepository.findAll();
        assertThat(services).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceRepository.findAll().size();
        // set the field null
        service.setStartTime(null);

        // Create the Service, which fails.

        restServiceMockMvc.perform(post("/api/services")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(service)))
                .andExpect(status().isBadRequest());

        List<Service> services = serviceRepository.findAll();
        assertThat(services).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceRepository.findAll().size();
        // set the field null
        service.setEndTime(null);

        // Create the Service, which fails.

        restServiceMockMvc.perform(post("/api/services")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(service)))
                .andExpect(status().isBadRequest());

        List<Service> services = serviceRepository.findAll();
        assertThat(services).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllServices() throws Exception {
        // Initialize the database
        serviceRepository.saveAndFlush(service);

        // Get all the services
        restServiceMockMvc.perform(get("/api/services?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(service.getId().intValue())))
                .andExpect(jsonPath("$.[*].mondayIsOpen").value(hasItem(DEFAULT_MONDAY_IS_OPEN.booleanValue())))
                .andExpect(jsonPath("$.[*].tuesdayIsOpen").value(hasItem(DEFAULT_TUESDAY_IS_OPEN.booleanValue())))
                .andExpect(jsonPath("$.[*].wednesdayIsOpen").value(hasItem(DEFAULT_WEDNESDAY_IS_OPEN.booleanValue())))
                .andExpect(jsonPath("$.[*].thursdayIsOpen").value(hasItem(DEFAULT_THURSDAY_IS_OPEN.booleanValue())))
                .andExpect(jsonPath("$.[*].fridayIsOpen").value(hasItem(DEFAULT_FRIDAY_IS_OPEN.booleanValue())))
                .andExpect(jsonPath("$.[*].saturdayIsOpen").value(hasItem(DEFAULT_SATURDAY_IS_OPEN.booleanValue())))
                .andExpect(jsonPath("$.[*].sundayIsOpen").value(hasItem(DEFAULT_SUNDAY_IS_OPEN.booleanValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].serviceType").value(hasItem(DEFAULT_SERVICE_TYPE.toString())))
                .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME_STR)))
                .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME_STR)));
    }

    @Test
    @Transactional
    public void getService() throws Exception {
        // Initialize the database
        serviceRepository.saveAndFlush(service);

        // Get the service
        restServiceMockMvc.perform(get("/api/services/{id}", service.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(service.getId().intValue()))
            .andExpect(jsonPath("$.mondayIsOpen").value(DEFAULT_MONDAY_IS_OPEN.booleanValue()))
            .andExpect(jsonPath("$.tuesdayIsOpen").value(DEFAULT_TUESDAY_IS_OPEN.booleanValue()))
            .andExpect(jsonPath("$.wednesdayIsOpen").value(DEFAULT_WEDNESDAY_IS_OPEN.booleanValue()))
            .andExpect(jsonPath("$.thursdayIsOpen").value(DEFAULT_THURSDAY_IS_OPEN.booleanValue()))
            .andExpect(jsonPath("$.fridayIsOpen").value(DEFAULT_FRIDAY_IS_OPEN.booleanValue()))
            .andExpect(jsonPath("$.saturdayIsOpen").value(DEFAULT_SATURDAY_IS_OPEN.booleanValue()))
            .andExpect(jsonPath("$.sundayIsOpen").value(DEFAULT_SUNDAY_IS_OPEN.booleanValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.serviceType").value(DEFAULT_SERVICE_TYPE.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME_STR))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingService() throws Exception {
        // Get the service
        restServiceMockMvc.perform(get("/api/services/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateService() throws Exception {
        // Initialize the database
        serviceRepository.saveAndFlush(service);
        serviceSearchRepository.save(service);
        int databaseSizeBeforeUpdate = serviceRepository.findAll().size();

        // Update the service
        Service updatedService = serviceRepository.findOne(service.getId());
        updatedService
                .mondayIsOpen(UPDATED_MONDAY_IS_OPEN)
                .tuesdayIsOpen(UPDATED_TUESDAY_IS_OPEN)
                .wednesdayIsOpen(UPDATED_WEDNESDAY_IS_OPEN)
                .thursdayIsOpen(UPDATED_THURSDAY_IS_OPEN)
                .fridayIsOpen(UPDATED_FRIDAY_IS_OPEN)
                .saturdayIsOpen(UPDATED_SATURDAY_IS_OPEN)
                .sundayIsOpen(UPDATED_SUNDAY_IS_OPEN)
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION)
                .serviceType(UPDATED_SERVICE_TYPE)
                .startTime(UPDATED_START_TIME)
                .endTime(UPDATED_END_TIME);

        restServiceMockMvc.perform(put("/api/services")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedService)))
                .andExpect(status().isOk());

        // Validate the Service in the database
        List<Service> services = serviceRepository.findAll();
        assertThat(services).hasSize(databaseSizeBeforeUpdate);
        Service testService = services.get(services.size() - 1);
        assertThat(testService.isMondayIsOpen()).isEqualTo(UPDATED_MONDAY_IS_OPEN);
        assertThat(testService.isTuesdayIsOpen()).isEqualTo(UPDATED_TUESDAY_IS_OPEN);
        assertThat(testService.isWednesdayIsOpen()).isEqualTo(UPDATED_WEDNESDAY_IS_OPEN);
        assertThat(testService.isThursdayIsOpen()).isEqualTo(UPDATED_THURSDAY_IS_OPEN);
        assertThat(testService.isFridayIsOpen()).isEqualTo(UPDATED_FRIDAY_IS_OPEN);
        assertThat(testService.isSaturdayIsOpen()).isEqualTo(UPDATED_SATURDAY_IS_OPEN);
        assertThat(testService.isSundayIsOpen()).isEqualTo(UPDATED_SUNDAY_IS_OPEN);
        assertThat(testService.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testService.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testService.getServiceType()).isEqualTo(UPDATED_SERVICE_TYPE);
        assertThat(testService.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testService.getEndTime()).isEqualTo(UPDATED_END_TIME);

        // Validate the Service in ElasticSearch
        Service serviceEs = serviceSearchRepository.findOne(testService.getId());
        assertThat(serviceEs).isEqualToComparingFieldByField(testService);
    }

    @Test
    @Transactional
    public void deleteService() throws Exception {
        // Initialize the database
        serviceRepository.saveAndFlush(service);
        serviceSearchRepository.save(service);
        int databaseSizeBeforeDelete = serviceRepository.findAll().size();

        // Get the service
        restServiceMockMvc.perform(delete("/api/services/{id}", service.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean serviceExistsInEs = serviceSearchRepository.exists(service.getId());
        assertThat(serviceExistsInEs).isFalse();

        // Validate the database is empty
        List<Service> services = serviceRepository.findAll();
        assertThat(services).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchService() throws Exception {
        // Initialize the database
        serviceRepository.saveAndFlush(service);
        serviceSearchRepository.save(service);

        // Search the service
        restServiceMockMvc.perform(get("/api/_search/services?query=id:" + service.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(service.getId().intValue())))
            .andExpect(jsonPath("$.[*].mondayIsOpen").value(hasItem(DEFAULT_MONDAY_IS_OPEN.booleanValue())))
            .andExpect(jsonPath("$.[*].tuesdayIsOpen").value(hasItem(DEFAULT_TUESDAY_IS_OPEN.booleanValue())))
            .andExpect(jsonPath("$.[*].wednesdayIsOpen").value(hasItem(DEFAULT_WEDNESDAY_IS_OPEN.booleanValue())))
            .andExpect(jsonPath("$.[*].thursdayIsOpen").value(hasItem(DEFAULT_THURSDAY_IS_OPEN.booleanValue())))
            .andExpect(jsonPath("$.[*].fridayIsOpen").value(hasItem(DEFAULT_FRIDAY_IS_OPEN.booleanValue())))
            .andExpect(jsonPath("$.[*].saturdayIsOpen").value(hasItem(DEFAULT_SATURDAY_IS_OPEN.booleanValue())))
            .andExpect(jsonPath("$.[*].sundayIsOpen").value(hasItem(DEFAULT_SUNDAY_IS_OPEN.booleanValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].serviceType").value(hasItem(DEFAULT_SERVICE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME_STR)))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME_STR)));
    }
}
