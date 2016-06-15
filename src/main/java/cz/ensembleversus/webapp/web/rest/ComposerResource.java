package cz.ensembleversus.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import cz.ensembleversus.webapp.domain.Composer;
import cz.ensembleversus.webapp.repository.ComposerRepository;
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
 * REST controller for managing Composer.
 */
@RestController
@RequestMapping("/api")
public class ComposerResource {

    private final Logger log = LoggerFactory.getLogger(ComposerResource.class);
        
    @Inject
    private ComposerRepository composerRepository;
    
    /**
     * POST  /composers : Create a new composer.
     *
     * @param composer the composer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new composer, or with status 400 (Bad Request) if the composer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/composers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Composer> createComposer(@Valid @RequestBody Composer composer) throws URISyntaxException {
        log.debug("REST request to save Composer : {}", composer);
        if (composer.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("composer", "idexists", "A new composer cannot already have an ID")).body(null);
        }
        Composer result = composerRepository.save(composer);
        return ResponseEntity.created(new URI("/api/composers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("composer", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /composers : Updates an existing composer.
     *
     * @param composer the composer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated composer,
     * or with status 400 (Bad Request) if the composer is not valid,
     * or with status 500 (Internal Server Error) if the composer couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/composers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Composer> updateComposer(@Valid @RequestBody Composer composer) throws URISyntaxException {
        log.debug("REST request to update Composer : {}", composer);
        if (composer.getId() == null) {
            return createComposer(composer);
        }
        Composer result = composerRepository.save(composer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("composer", composer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /composers : get all the composers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of composers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/composers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Composer>> getAllComposers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Composers");
        Page<Composer> page = composerRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/composers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /composers/:id : get the "id" composer.
     *
     * @param id the id of the composer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the composer, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/composers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Composer> getComposer(@PathVariable Long id) {
        log.debug("REST request to get Composer : {}", id);
        Composer composer = composerRepository.findOne(id);
        return Optional.ofNullable(composer)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /composers/:id : delete the "id" composer.
     *
     * @param id the id of the composer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/composers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteComposer(@PathVariable Long id) {
        log.debug("REST request to delete Composer : {}", id);
        composerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("composer", id.toString())).build();
    }

}
