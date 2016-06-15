package cz.ensembleversus.webapp.web.rest;

import cz.ensembleversus.webapp.VersushipsterApp;
import cz.ensembleversus.webapp.domain.TranslationKey;
import cz.ensembleversus.webapp.repository.TranslationKeyRepository;

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
 * Test class for the TranslationKeyResource REST controller.
 *
 * @see TranslationKeyResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VersushipsterApp.class)
@WebAppConfiguration
@IntegrationTest
public class TranslationKeyResourceIntTest {

    private static final String DEFAULT_KEY = "AAAAA";
    private static final String UPDATED_KEY = "BBBBB";

    @Inject
    private TranslationKeyRepository translationKeyRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTranslationKeyMockMvc;

    private TranslationKey translationKey;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TranslationKeyResource translationKeyResource = new TranslationKeyResource();
        ReflectionTestUtils.setField(translationKeyResource, "translationKeyRepository", translationKeyRepository);
        this.restTranslationKeyMockMvc = MockMvcBuilders.standaloneSetup(translationKeyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        translationKey = new TranslationKey();
        translationKey.setKey(DEFAULT_KEY);
    }

    @Test
    @Transactional
    public void createTranslationKey() throws Exception {
        int databaseSizeBeforeCreate = translationKeyRepository.findAll().size();

        // Create the TranslationKey

        restTranslationKeyMockMvc.perform(post("/api/translation-keys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(translationKey)))
                .andExpect(status().isCreated());

        // Validate the TranslationKey in the database
        List<TranslationKey> translationKeys = translationKeyRepository.findAll();
        assertThat(translationKeys).hasSize(databaseSizeBeforeCreate + 1);
        TranslationKey testTranslationKey = translationKeys.get(translationKeys.size() - 1);
        assertThat(testTranslationKey.getKey()).isEqualTo(DEFAULT_KEY);
    }

    @Test
    @Transactional
    public void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = translationKeyRepository.findAll().size();
        // set the field null
        translationKey.setKey(null);

        // Create the TranslationKey, which fails.

        restTranslationKeyMockMvc.perform(post("/api/translation-keys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(translationKey)))
                .andExpect(status().isBadRequest());

        List<TranslationKey> translationKeys = translationKeyRepository.findAll();
        assertThat(translationKeys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTranslationKeys() throws Exception {
        // Initialize the database
        translationKeyRepository.saveAndFlush(translationKey);

        // Get all the translationKeys
        restTranslationKeyMockMvc.perform(get("/api/translation-keys?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(translationKey.getId().intValue())))
                .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())));
    }

    @Test
    @Transactional
    public void getTranslationKey() throws Exception {
        // Initialize the database
        translationKeyRepository.saveAndFlush(translationKey);

        // Get the translationKey
        restTranslationKeyMockMvc.perform(get("/api/translation-keys/{id}", translationKey.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(translationKey.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTranslationKey() throws Exception {
        // Get the translationKey
        restTranslationKeyMockMvc.perform(get("/api/translation-keys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTranslationKey() throws Exception {
        // Initialize the database
        translationKeyRepository.saveAndFlush(translationKey);
        int databaseSizeBeforeUpdate = translationKeyRepository.findAll().size();

        // Update the translationKey
        TranslationKey updatedTranslationKey = new TranslationKey();
        updatedTranslationKey.setId(translationKey.getId());
        updatedTranslationKey.setKey(UPDATED_KEY);

        restTranslationKeyMockMvc.perform(put("/api/translation-keys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTranslationKey)))
                .andExpect(status().isOk());

        // Validate the TranslationKey in the database
        List<TranslationKey> translationKeys = translationKeyRepository.findAll();
        assertThat(translationKeys).hasSize(databaseSizeBeforeUpdate);
        TranslationKey testTranslationKey = translationKeys.get(translationKeys.size() - 1);
        assertThat(testTranslationKey.getKey()).isEqualTo(UPDATED_KEY);
    }

    @Test
    @Transactional
    public void deleteTranslationKey() throws Exception {
        // Initialize the database
        translationKeyRepository.saveAndFlush(translationKey);
        int databaseSizeBeforeDelete = translationKeyRepository.findAll().size();

        // Get the translationKey
        restTranslationKeyMockMvc.perform(delete("/api/translation-keys/{id}", translationKey.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TranslationKey> translationKeys = translationKeyRepository.findAll();
        assertThat(translationKeys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
