package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Test3App;

import com.mycompany.myapp.domain.Holiday;
import com.mycompany.myapp.repository.HolidayRepository;
import com.mycompany.myapp.repository.search.HolidaySearchRepository;

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
 * Test class for the HolidayResource REST controller.
 *
 * @see HolidayResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Test3App.class)
public class HolidayResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_NAME = "AA";
    private static final String UPDATED_NAME = "BB";

    private static final ZonedDateTime DEFAULT_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_START_TIME_STR = dateTimeFormatter.format(DEFAULT_START_TIME);

    private static final ZonedDateTime DEFAULT_END_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_END_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_END_TIME_STR = dateTimeFormatter.format(DEFAULT_END_TIME);

    @Inject
    private HolidayRepository holidayRepository;

    @Inject
    private HolidaySearchRepository holidaySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restHolidayMockMvc;

    private Holiday holiday;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HolidayResource holidayResource = new HolidayResource();
        ReflectionTestUtils.setField(holidayResource, "holidaySearchRepository", holidaySearchRepository);
        ReflectionTestUtils.setField(holidayResource, "holidayRepository", holidayRepository);
        this.restHolidayMockMvc = MockMvcBuilders.standaloneSetup(holidayResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Holiday createEntity(EntityManager em) {
        Holiday holiday = new Holiday()
                .name(DEFAULT_NAME)
                .startTime(DEFAULT_START_TIME)
                .endTime(DEFAULT_END_TIME);
        return holiday;
    }

    @Before
    public void initTest() {
        holidaySearchRepository.deleteAll();
        holiday = createEntity(em);
    }

    @Test
    @Transactional
    public void createHoliday() throws Exception {
        int databaseSizeBeforeCreate = holidayRepository.findAll().size();

        // Create the Holiday

        restHolidayMockMvc.perform(post("/api/holidays")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(holiday)))
                .andExpect(status().isCreated());

        // Validate the Holiday in the database
        List<Holiday> holidays = holidayRepository.findAll();
        assertThat(holidays).hasSize(databaseSizeBeforeCreate + 1);
        Holiday testHoliday = holidays.get(holidays.size() - 1);
        assertThat(testHoliday.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHoliday.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testHoliday.getEndTime()).isEqualTo(DEFAULT_END_TIME);

        // Validate the Holiday in ElasticSearch
        Holiday holidayEs = holidaySearchRepository.findOne(testHoliday.getId());
        assertThat(holidayEs).isEqualToComparingFieldByField(testHoliday);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = holidayRepository.findAll().size();
        // set the field null
        holiday.setName(null);

        // Create the Holiday, which fails.

        restHolidayMockMvc.perform(post("/api/holidays")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(holiday)))
                .andExpect(status().isBadRequest());

        List<Holiday> holidays = holidayRepository.findAll();
        assertThat(holidays).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = holidayRepository.findAll().size();
        // set the field null
        holiday.setStartTime(null);

        // Create the Holiday, which fails.

        restHolidayMockMvc.perform(post("/api/holidays")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(holiday)))
                .andExpect(status().isBadRequest());

        List<Holiday> holidays = holidayRepository.findAll();
        assertThat(holidays).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = holidayRepository.findAll().size();
        // set the field null
        holiday.setEndTime(null);

        // Create the Holiday, which fails.

        restHolidayMockMvc.perform(post("/api/holidays")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(holiday)))
                .andExpect(status().isBadRequest());

        List<Holiday> holidays = holidayRepository.findAll();
        assertThat(holidays).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHolidays() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        // Get all the holidays
        restHolidayMockMvc.perform(get("/api/holidays?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(holiday.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME_STR)))
                .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME_STR)));
    }

    @Test
    @Transactional
    public void getHoliday() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        // Get the holiday
        restHolidayMockMvc.perform(get("/api/holidays/{id}", holiday.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(holiday.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME_STR))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingHoliday() throws Exception {
        // Get the holiday
        restHolidayMockMvc.perform(get("/api/holidays/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHoliday() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);
        holidaySearchRepository.save(holiday);
        int databaseSizeBeforeUpdate = holidayRepository.findAll().size();

        // Update the holiday
        Holiday updatedHoliday = holidayRepository.findOne(holiday.getId());
        updatedHoliday
                .name(UPDATED_NAME)
                .startTime(UPDATED_START_TIME)
                .endTime(UPDATED_END_TIME);

        restHolidayMockMvc.perform(put("/api/holidays")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedHoliday)))
                .andExpect(status().isOk());

        // Validate the Holiday in the database
        List<Holiday> holidays = holidayRepository.findAll();
        assertThat(holidays).hasSize(databaseSizeBeforeUpdate);
        Holiday testHoliday = holidays.get(holidays.size() - 1);
        assertThat(testHoliday.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHoliday.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testHoliday.getEndTime()).isEqualTo(UPDATED_END_TIME);

        // Validate the Holiday in ElasticSearch
        Holiday holidayEs = holidaySearchRepository.findOne(testHoliday.getId());
        assertThat(holidayEs).isEqualToComparingFieldByField(testHoliday);
    }

    @Test
    @Transactional
    public void deleteHoliday() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);
        holidaySearchRepository.save(holiday);
        int databaseSizeBeforeDelete = holidayRepository.findAll().size();

        // Get the holiday
        restHolidayMockMvc.perform(delete("/api/holidays/{id}", holiday.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean holidayExistsInEs = holidaySearchRepository.exists(holiday.getId());
        assertThat(holidayExistsInEs).isFalse();

        // Validate the database is empty
        List<Holiday> holidays = holidayRepository.findAll();
        assertThat(holidays).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchHoliday() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);
        holidaySearchRepository.save(holiday);

        // Search the holiday
        restHolidayMockMvc.perform(get("/api/_search/holidays?query=id:" + holiday.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(holiday.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME_STR)))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME_STR)));
    }
}
