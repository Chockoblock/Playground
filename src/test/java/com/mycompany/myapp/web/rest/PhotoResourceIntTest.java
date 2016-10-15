package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Test3App;

import com.mycompany.myapp.domain.Photo;
import com.mycompany.myapp.repository.PhotoRepository;
import com.mycompany.myapp.repository.search.PhotoSearchRepository;

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
 * Test class for the PhotoResource REST controller.
 *
 * @see PhotoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Test3App.class)
public class PhotoResourceIntTest {

    private static final String DEFAULT_LOCATION = "AAAAA";
    private static final String UPDATED_LOCATION = "BBBBB";

    private static final Boolean DEFAULT_IS_PROFILE_PHOTO = false;
    private static final Boolean UPDATED_IS_PROFILE_PHOTO = true;
    private static final String DEFAULT_TAG = "AAAAA";
    private static final String UPDATED_TAG = "BBBBB";

    @Inject
    private PhotoRepository photoRepository;

    @Inject
    private PhotoSearchRepository photoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPhotoMockMvc;

    private Photo photo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PhotoResource photoResource = new PhotoResource();
        ReflectionTestUtils.setField(photoResource, "photoSearchRepository", photoSearchRepository);
        ReflectionTestUtils.setField(photoResource, "photoRepository", photoRepository);
        this.restPhotoMockMvc = MockMvcBuilders.standaloneSetup(photoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Photo createEntity(EntityManager em) {
        Photo photo = new Photo()
                .location(DEFAULT_LOCATION)
                .isProfilePhoto(DEFAULT_IS_PROFILE_PHOTO)
                .tag(DEFAULT_TAG);
        return photo;
    }

    @Before
    public void initTest() {
        photoSearchRepository.deleteAll();
        photo = createEntity(em);
    }

    @Test
    @Transactional
    public void createPhoto() throws Exception {
        int databaseSizeBeforeCreate = photoRepository.findAll().size();

        // Create the Photo

        restPhotoMockMvc.perform(post("/api/photos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photo)))
                .andExpect(status().isCreated());

        // Validate the Photo in the database
        List<Photo> photos = photoRepository.findAll();
        assertThat(photos).hasSize(databaseSizeBeforeCreate + 1);
        Photo testPhoto = photos.get(photos.size() - 1);
        assertThat(testPhoto.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testPhoto.isIsProfilePhoto()).isEqualTo(DEFAULT_IS_PROFILE_PHOTO);
        assertThat(testPhoto.getTag()).isEqualTo(DEFAULT_TAG);

        // Validate the Photo in ElasticSearch
        Photo photoEs = photoSearchRepository.findOne(testPhoto.getId());
        assertThat(photoEs).isEqualToComparingFieldByField(testPhoto);
    }

    @Test
    @Transactional
    public void getAllPhotos() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photos
        restPhotoMockMvc.perform(get("/api/photos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(photo.getId().intValue())))
                .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
                .andExpect(jsonPath("$.[*].isProfilePhoto").value(hasItem(DEFAULT_IS_PROFILE_PHOTO.booleanValue())))
                .andExpect(jsonPath("$.[*].tag").value(hasItem(DEFAULT_TAG.toString())));
    }

    @Test
    @Transactional
    public void getPhoto() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get the photo
        restPhotoMockMvc.perform(get("/api/photos/{id}", photo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(photo.getId().intValue()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.isProfilePhoto").value(DEFAULT_IS_PROFILE_PHOTO.booleanValue()))
            .andExpect(jsonPath("$.tag").value(DEFAULT_TAG.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPhoto() throws Exception {
        // Get the photo
        restPhotoMockMvc.perform(get("/api/photos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePhoto() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);
        photoSearchRepository.save(photo);
        int databaseSizeBeforeUpdate = photoRepository.findAll().size();

        // Update the photo
        Photo updatedPhoto = photoRepository.findOne(photo.getId());
        updatedPhoto
                .location(UPDATED_LOCATION)
                .isProfilePhoto(UPDATED_IS_PROFILE_PHOTO)
                .tag(UPDATED_TAG);

        restPhotoMockMvc.perform(put("/api/photos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPhoto)))
                .andExpect(status().isOk());

        // Validate the Photo in the database
        List<Photo> photos = photoRepository.findAll();
        assertThat(photos).hasSize(databaseSizeBeforeUpdate);
        Photo testPhoto = photos.get(photos.size() - 1);
        assertThat(testPhoto.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testPhoto.isIsProfilePhoto()).isEqualTo(UPDATED_IS_PROFILE_PHOTO);
        assertThat(testPhoto.getTag()).isEqualTo(UPDATED_TAG);

        // Validate the Photo in ElasticSearch
        Photo photoEs = photoSearchRepository.findOne(testPhoto.getId());
        assertThat(photoEs).isEqualToComparingFieldByField(testPhoto);
    }

    @Test
    @Transactional
    public void deletePhoto() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);
        photoSearchRepository.save(photo);
        int databaseSizeBeforeDelete = photoRepository.findAll().size();

        // Get the photo
        restPhotoMockMvc.perform(delete("/api/photos/{id}", photo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean photoExistsInEs = photoSearchRepository.exists(photo.getId());
        assertThat(photoExistsInEs).isFalse();

        // Validate the database is empty
        List<Photo> photos = photoRepository.findAll();
        assertThat(photos).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPhoto() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);
        photoSearchRepository.save(photo);

        // Search the photo
        restPhotoMockMvc.perform(get("/api/_search/photos?query=id:" + photo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(photo.getId().intValue())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].isProfilePhoto").value(hasItem(DEFAULT_IS_PROFILE_PHOTO.booleanValue())))
            .andExpect(jsonPath("$.[*].tag").value(hasItem(DEFAULT_TAG.toString())));
    }
}
