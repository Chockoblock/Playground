package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Test3App;

import com.mycompany.myapp.domain.Resource;
import com.mycompany.myapp.repository.ResourceRepository;
import com.mycompany.myapp.repository.search.ResourceSearchRepository;

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
 * Test class for the ResourceResource REST controller.
 *
 * @see ResourceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Test3App.class)
public class ResourceResourceIntTest {

    private static final String DEFAULT_NAME = "A";
    private static final String UPDATED_NAME = "B";

    private static final Integer DEFAULT_SIZE = 0;
    private static final Integer UPDATED_SIZE = 1;

    @Inject
    private ResourceRepository resourceRepository;

    @Inject
    private ResourceSearchRepository resourceSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restResourceMockMvc;

    private Resource resource;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ResourceResource resourceResource = new ResourceResource();
        ReflectionTestUtils.setField(resourceResource, "resourceSearchRepository", resourceSearchRepository);
        ReflectionTestUtils.setField(resourceResource, "resourceRepository", resourceRepository);
        this.restResourceMockMvc = MockMvcBuilders.standaloneSetup(resourceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resource createEntity(EntityManager em) {
        Resource resource = new Resource()
                .name(DEFAULT_NAME)
                .size(DEFAULT_SIZE);
        return resource;
    }

    @Before
    public void initTest() {
        resourceSearchRepository.deleteAll();
        resource = createEntity(em);
    }

    @Test
    @Transactional
    public void createResource() throws Exception {
        int databaseSizeBeforeCreate = resourceRepository.findAll().size();

        // Create the Resource

        restResourceMockMvc.perform(post("/api/resources")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(resource)))
                .andExpect(status().isCreated());

        // Validate the Resource in the database
        List<Resource> resources = resourceRepository.findAll();
        assertThat(resources).hasSize(databaseSizeBeforeCreate + 1);
        Resource testResource = resources.get(resources.size() - 1);
        assertThat(testResource.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testResource.getSize()).isEqualTo(DEFAULT_SIZE);

        // Validate the Resource in ElasticSearch
        Resource resourceEs = resourceSearchRepository.findOne(testResource.getId());
        assertThat(resourceEs).isEqualToComparingFieldByField(testResource);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = resourceRepository.findAll().size();
        // set the field null
        resource.setName(null);

        // Create the Resource, which fails.

        restResourceMockMvc.perform(post("/api/resources")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(resource)))
                .andExpect(status().isBadRequest());

        List<Resource> resources = resourceRepository.findAll();
        assertThat(resources).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSizeIsRequired() throws Exception {
        int databaseSizeBeforeTest = resourceRepository.findAll().size();
        // set the field null
        resource.setSize(null);

        // Create the Resource, which fails.

        restResourceMockMvc.perform(post("/api/resources")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(resource)))
                .andExpect(status().isBadRequest());

        List<Resource> resources = resourceRepository.findAll();
        assertThat(resources).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllResources() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resources
        restResourceMockMvc.perform(get("/api/resources?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(resource.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE)));
    }

    @Test
    @Transactional
    public void getResource() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get the resource
        restResourceMockMvc.perform(get("/api/resources/{id}", resource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(resource.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.size").value(DEFAULT_SIZE));
    }

    @Test
    @Transactional
    public void getNonExistingResource() throws Exception {
        // Get the resource
        restResourceMockMvc.perform(get("/api/resources/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResource() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);
        resourceSearchRepository.save(resource);
        int databaseSizeBeforeUpdate = resourceRepository.findAll().size();

        // Update the resource
        Resource updatedResource = resourceRepository.findOne(resource.getId());
        updatedResource
                .name(UPDATED_NAME)
                .size(UPDATED_SIZE);

        restResourceMockMvc.perform(put("/api/resources")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedResource)))
                .andExpect(status().isOk());

        // Validate the Resource in the database
        List<Resource> resources = resourceRepository.findAll();
        assertThat(resources).hasSize(databaseSizeBeforeUpdate);
        Resource testResource = resources.get(resources.size() - 1);
        assertThat(testResource.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testResource.getSize()).isEqualTo(UPDATED_SIZE);

        // Validate the Resource in ElasticSearch
        Resource resourceEs = resourceSearchRepository.findOne(testResource.getId());
        assertThat(resourceEs).isEqualToComparingFieldByField(testResource);
    }

    @Test
    @Transactional
    public void deleteResource() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);
        resourceSearchRepository.save(resource);
        int databaseSizeBeforeDelete = resourceRepository.findAll().size();

        // Get the resource
        restResourceMockMvc.perform(delete("/api/resources/{id}", resource.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean resourceExistsInEs = resourceSearchRepository.exists(resource.getId());
        assertThat(resourceExistsInEs).isFalse();

        // Validate the database is empty
        List<Resource> resources = resourceRepository.findAll();
        assertThat(resources).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchResource() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);
        resourceSearchRepository.save(resource);

        // Search the resource
        restResourceMockMvc.perform(get("/api/_search/resources?query=id:" + resource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resource.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE)));
    }
}
