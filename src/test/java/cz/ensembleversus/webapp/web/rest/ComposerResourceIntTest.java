package cz.ensembleversus.webapp.web.rest;

import cz.ensembleversus.webapp.VersushipsterApp;
import cz.ensembleversus.webapp.domain.Composer;
import cz.ensembleversus.webapp.repository.ComposerRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ComposerResource REST controller.
 *
 * @see ComposerResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VersushipsterApp.class)
@WebAppConfiguration
@IntegrationTest
public class ComposerResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private ComposerRepository composerRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restComposerMockMvc;

    private Composer composer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ComposerResource composerResource = new ComposerResource();
        ReflectionTestUtils.setField(composerResource, "composerRepository", composerRepository);
        this.restComposerMockMvc = MockMvcBuilders.standaloneSetup(composerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        composer = new Composer();
        composer.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createComposer() throws Exception {
        int databaseSizeBeforeCreate = composerRepository.findAll().size();

        // Create the Composer

        restComposerMockMvc.perform(post("/api/composers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(composer)))
                .andExpect(status().isCreated());

        // Validate the Composer in the database
        List<Composer> composers = composerRepository.findAll();
        assertThat(composers).hasSize(databaseSizeBeforeCreate + 1);
        Composer testComposer = composers.get(composers.size() - 1);
        assertThat(testComposer.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = composerRepository.findAll().size();
        // set the field null
        composer.setName(null);

        // Create the Composer, which fails.

        restComposerMockMvc.perform(post("/api/composers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(composer)))
                .andExpect(status().isBadRequest());

        List<Composer> composers = composerRepository.findAll();
        assertThat(composers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllComposers() throws Exception {
        // Initialize the database
        composerRepository.saveAndFlush(composer);

        // Get all the composers
        restComposerMockMvc.perform(get("/api/composers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(composer.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getComposer() throws Exception {
        // Initialize the database
        composerRepository.saveAndFlush(composer);

        // Get the composer
        restComposerMockMvc.perform(get("/api/composers/{id}", composer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(composer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingComposer() throws Exception {
        // Get the composer
        restComposerMockMvc.perform(get("/api/composers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateComposer() throws Exception {
        // Initialize the database
        composerRepository.saveAndFlush(composer);
        int databaseSizeBeforeUpdate = composerRepository.findAll().size();

        // Update the composer
        Composer updatedComposer = new Composer();
        updatedComposer.setId(composer.getId());
        updatedComposer.setName(UPDATED_NAME);

        restComposerMockMvc.perform(put("/api/composers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedComposer)))
                .andExpect(status().isOk());

        // Validate the Composer in the database
        List<Composer> composers = composerRepository.findAll();
        assertThat(composers).hasSize(databaseSizeBeforeUpdate);
        Composer testComposer = composers.get(composers.size() - 1);
        assertThat(testComposer.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteComposer() throws Exception {
        // Initialize the database
        composerRepository.saveAndFlush(composer);
        int databaseSizeBeforeDelete = composerRepository.findAll().size();

        // Get the composer
        restComposerMockMvc.perform(delete("/api/composers/{id}", composer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Composer> composers = composerRepository.findAll();
        assertThat(composers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
