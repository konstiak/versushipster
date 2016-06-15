package cz.ensembleversus.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import cz.ensembleversus.webapp.domain.TranslationKey;
import cz.ensembleversus.webapp.repository.TranslationKeyRepository;
import cz.ensembleversus.webapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TranslationKey.
 */
@RestController
@RequestMapping("/api")
public class TranslationKeyResource {

    private final Logger log = LoggerFactory.getLogger(TranslationKeyResource.class);
        
    @Inject
    private TranslationKeyRepository translationKeyRepository;
    
    /**
     * POST  /translation-keys : Create a new translationKey.
     *
     * @param translationKey the translationKey to create
     * @return the ResponseEntity with status 201 (Created) and with body the new translationKey, or with status 400 (Bad Request) if the translationKey has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/translation-keys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TranslationKey> createTranslationKey(@Valid @RequestBody TranslationKey translationKey) throws URISyntaxException {
        log.debug("REST request to save TranslationKey : {}", translationKey);
        if (translationKey.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("translationKey", "idexists", "A new translationKey cannot already have an ID")).body(null);
        }
        TranslationKey result = translationKeyRepository.save(translationKey);
        return ResponseEntity.created(new URI("/api/translation-keys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("translationKey", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /translation-keys : Updates an existing translationKey.
     *
     * @param translationKey the translationKey to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated translationKey,
     * or with status 400 (Bad Request) if the translationKey is not valid,
     * or with status 500 (Internal Server Error) if the translationKey couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/translation-keys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TranslationKey> updateTranslationKey(@Valid @RequestBody TranslationKey translationKey) throws URISyntaxException {
        log.debug("REST request to update TranslationKey : {}", translationKey);
        if (translationKey.getId() == null) {
            return createTranslationKey(translationKey);
        }
        TranslationKey result = translationKeyRepository.save(translationKey);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("translationKey", translationKey.getId().toString()))
            .body(result);
    }

    /**
     * GET  /translation-keys : get all the translationKeys.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of translationKeys in body
     */
    @RequestMapping(value = "/translation-keys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TranslationKey> getAllTranslationKeys() {
        log.debug("REST request to get all TranslationKeys");
        List<TranslationKey> translationKeys = translationKeyRepository.findAll();
        return translationKeys;
    }

    /**
     * GET  /translation-keys/:id : get the "id" translationKey.
     *
     * @param id the id of the translationKey to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the translationKey, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/translation-keys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TranslationKey> getTranslationKey(@PathVariable Long id) {
        log.debug("REST request to get TranslationKey : {}", id);
        TranslationKey translationKey = translationKeyRepository.findOne(id);
        return Optional.ofNullable(translationKey)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /translation-keys/:id : delete the "id" translationKey.
     *
     * @param id the id of the translationKey to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/translation-keys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTranslationKey(@PathVariable Long id) {
        log.debug("REST request to delete TranslationKey : {}", id);
        translationKeyRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("translationKey", id.toString())).build();
    }

}
