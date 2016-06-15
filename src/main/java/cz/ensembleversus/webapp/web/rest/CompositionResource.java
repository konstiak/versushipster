package cz.ensembleversus.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import cz.ensembleversus.webapp.domain.Composition;
import cz.ensembleversus.webapp.repository.CompositionRepository;
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
 * REST controller for managing Composition.
 */
@RestController
@RequestMapping("/api")
public class CompositionResource {

    private final Logger log = LoggerFactory.getLogger(CompositionResource.class);
        
    @Inject
    private CompositionRepository compositionRepository;
    
    /**
     * POST  /compositions : Create a new composition.
     *
     * @param composition the composition to create
     * @return the ResponseEntity with status 201 (Created) and with body the new composition, or with status 400 (Bad Request) if the composition has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/compositions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Composition> createComposition(@Valid @RequestBody Composition composition) throws URISyntaxException {
        log.debug("REST request to save Composition : {}", composition);
        if (composition.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("composition", "idexists", "A new composition cannot already have an ID")).body(null);
        }
        Composition result = compositionRepository.save(composition);
        return ResponseEntity.created(new URI("/api/compositions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("composition", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /compositions : Updates an existing composition.
     *
     * @param composition the composition to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated composition,
     * or with status 400 (Bad Request) if the composition is not valid,
     * or with status 500 (Internal Server Error) if the composition couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/compositions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Composition> updateComposition(@Valid @RequestBody Composition composition) throws URISyntaxException {
        log.debug("REST request to update Composition : {}", composition);
        if (composition.getId() == null) {
            return createComposition(composition);
        }
        Composition result = compositionRepository.save(composition);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("composition", composition.getId().toString()))
            .body(result);
    }

    /**
     * GET  /compositions : get all the compositions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of compositions in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/compositions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Composition>> getAllCompositions(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Compositions");
        Page<Composition> page = compositionRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/compositions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /compositions/:id : get the "id" composition.
     *
     * @param id the id of the composition to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the composition, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/compositions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Composition> getComposition(@PathVariable Long id) {
        log.debug("REST request to get Composition : {}", id);
        Composition composition = compositionRepository.findOne(id);
        return Optional.ofNullable(composition)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /compositions/:id : delete the "id" composition.
     *
     * @param id the id of the composition to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/compositions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteComposition(@PathVariable Long id) {
        log.debug("REST request to delete Composition : {}", id);
        compositionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("composition", id.toString())).build();
    }

}
