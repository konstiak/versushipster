package cz.ensembleversus.webapp.web.rest;

import cz.ensembleversus.webapp.VersushipsterApp;
import cz.ensembleversus.webapp.domain.Composition;
import cz.ensembleversus.webapp.repository.CompositionRepository;

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
 * Test class for the CompositionResource REST controller.
 *
 * @see CompositionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VersushipsterApp.class)
@WebAppConfiguration
@IntegrationTest
public class CompositionResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";
    private static final String DEFAULT_ORIGINAL_LYRICS = "AAAAA";
    private static final String UPDATED_ORIGINAL_LYRICS = "BBBBB";

    @Inject
    private CompositionRepository compositionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCompositionMockMvc;

    private Composition composition;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CompositionResource compositionResource = new CompositionResource();
        ReflectionTestUtils.setField(compositionResource, "compositionRepository", compositionRepository);
        this.restCompositionMockMvc = MockMvcBuilders.standaloneSetup(compositionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        composition = new Composition();
        composition.setTitle(DEFAULT_TITLE);
        composition.setOriginalLyrics(DEFAULT_ORIGINAL_LYRICS);
    }

    @Test
    @Transactional
    public void createComposition() throws Exception {
        int databaseSizeBeforeCreate = compositionRepository.findAll().size();

        // Create the Composition

        restCompositionMockMvc.perform(post("/api/compositions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(composition)))
                .andExpect(status().isCreated());

        // Validate the Composition in the database
        List<Composition> compositions = compositionRepository.findAll();
        assertThat(compositions).hasSize(databaseSizeBeforeCreate + 1);
        Composition testComposition = compositions.get(compositions.size() - 1);
        assertThat(testComposition.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testComposition.getOriginalLyrics()).isEqualTo(DEFAULT_ORIGINAL_LYRICS);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = compositionRepository.findAll().size();
        // set the field null
        composition.setTitle(null);

        // Create the Composition, which fails.

        restCompositionMockMvc.perform(post("/api/compositions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(composition)))
                .andExpect(status().isBadRequest());

        List<Composition> compositions = compositionRepository.findAll();
        assertThat(compositions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCompositions() throws Exception {
        // Initialize the database
        compositionRepository.saveAndFlush(composition);

        // Get all the compositions
        restCompositionMockMvc.perform(get("/api/compositions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(composition.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].originalLyrics").value(hasItem(DEFAULT_ORIGINAL_LYRICS.toString())));
    }

    @Test
    @Transactional
    public void getComposition() throws Exception {
        // Initialize the database
        compositionRepository.saveAndFlush(composition);

        // Get the composition
        restCompositionMockMvc.perform(get("/api/compositions/{id}", composition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(composition.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.originalLyrics").value(DEFAULT_ORIGINAL_LYRICS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingComposition() throws Exception {
        // Get the composition
        restCompositionMockMvc.perform(get("/api/compositions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateComposition() throws Exception {
        // Initialize the database
        compositionRepository.saveAndFlush(composition);
        int databaseSizeBeforeUpdate = compositionRepository.findAll().size();

        // Update the composition
        Composition updatedComposition = new Composition();
        updatedComposition.setId(composition.getId());
        updatedComposition.setTitle(UPDATED_TITLE);
        updatedComposition.setOriginalLyrics(UPDATED_ORIGINAL_LYRICS);

        restCompositionMockMvc.perform(put("/api/compositions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedComposition)))
                .andExpect(status().isOk());

        // Validate the Composition in the database
        List<Composition> compositions = compositionRepository.findAll();
        assertThat(compositions).hasSize(databaseSizeBeforeUpdate);
        Composition testComposition = compositions.get(compositions.size() - 1);
        assertThat(testComposition.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testComposition.getOriginalLyrics()).isEqualTo(UPDATED_ORIGINAL_LYRICS);
    }

    @Test
    @Transactional
    public void deleteComposition() throws Exception {
        // Initialize the database
        compositionRepository.saveAndFlush(composition);
        int databaseSizeBeforeDelete = compositionRepository.findAll().size();

        // Get the composition
        restCompositionMockMvc.perform(delete("/api/compositions/{id}", composition.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Composition> compositions = compositionRepository.findAll();
        assertThat(compositions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
