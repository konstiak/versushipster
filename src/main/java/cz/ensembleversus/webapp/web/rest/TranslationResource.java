package cz.ensembleversus.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import cz.ensembleversus.webapp.domain.Translation;
import cz.ensembleversus.webapp.repository.TranslationRepository;
import cz.ensembleversus.webapp.web.rest.util.HeaderUtil;
import cz.ensembleversus.webapp.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
 * REST controller for managing Translation.
 */
@RestController
@RequestMapping("/api")
public class TranslationResource {

    private final Logger log = LoggerFactory.getLogger(TranslationResource.class);
        
    @Inject
    private TranslationRepository translationRepository;
    
    /**
     * POST  /translations : Create a new translation.
     *
     * @param translation the translation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new translation, or with status 400 (Bad Request) if the translation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/translations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Translation> createTranslation(@Valid @RequestBody Translation translation) throws URISyntaxException {
        log.debug("REST request to save Translation : {}", translation);
        if (translation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("translation", "idexists", "A new translation cannot already have an ID")).body(null);
        }
        Translation result = translationRepository.save(translation);
        return ResponseEntity.created(new URI("/api/translations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("translation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /translations : Updates an existing translation.
     *
     * @param translation the translation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated translation,
     * or with status 400 (Bad Request) if the translation is not valid,
     * or with status 500 (Internal Server Error) if the translation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/translations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Translation> updateTranslation(@Valid @RequestBody Translation translation) throws URISyntaxException {
        log.debug("REST request to update Translation : {}", translation);
        if (translation.getId() == null) {
            return createTranslation(translation);
        }
        Translation result = translationRepository.save(translation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("translation", translation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /translations : get all the translations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of translations in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/translations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Translation>> getAllTranslations(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Translations");
        Page<Translation> page = translationRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/translations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /translations/:id : get the "id" translation.
     *
     * @param id the id of the translation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the translation, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/translations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Translation> getTranslation(@PathVariable Long id) {
        log.debug("REST request to get Translation : {}", id);
        Translation translation = translationRepository.findOne(id);
        return Optional.ofNullable(translation)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /translations/:id : delete the "id" translation.
     *
     * @param id the id of the translation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/translations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTranslation(@PathVariable Long id) {
        log.debug("REST request to delete Translation : {}", id);
        translationRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("translation", id.toString())).build();
    }

}
