package cz.ensembleversus.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import cz.ensembleversus.webapp.domain.Program;
import cz.ensembleversus.webapp.repository.ProgramRepository;
import cz.ensembleversus.webapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Program.
 */
@RestController
@RequestMapping("/api")
public class ProgramResource {

    private final Logger log = LoggerFactory.getLogger(ProgramResource.class);
        
    @Inject
    private ProgramRepository programRepository;
    
    /**
     * POST  /programs : Create a new program.
     *
     * @param program the program to create
     * @return the ResponseEntity with status 201 (Created) and with body the new program, or with status 400 (Bad Request) if the program has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/programs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Program> createProgram(@RequestBody Program program) throws URISyntaxException {
        log.debug("REST request to save Program : {}", program);
        if (program.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("program", "idexists", "A new program cannot already have an ID")).body(null);
        }
        Program result = programRepository.save(program);
        return ResponseEntity.created(new URI("/api/programs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("program", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /programs : Updates an existing program.
     *
     * @param program the program to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated program,
     * or with status 400 (Bad Request) if the program is not valid,
     * or with status 500 (Internal Server Error) if the program couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/programs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Program> updateProgram(@RequestBody Program program) throws URISyntaxException {
        log.debug("REST request to update Program : {}", program);
        if (program.getId() == null) {
            return createProgram(program);
        }
        Program result = programRepository.save(program);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("program", program.getId().toString()))
            .body(result);
    }

    /**
     * GET  /programs : get all the programs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of programs in body
     */
    @RequestMapping(value = "/programs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Program> getAllPrograms() {
        log.debug("REST request to get all Programs");
        List<Program> programs = programRepository.findAllWithEagerRelationships();
        return programs;
    }

    /**
     * GET  /programs/:id : get the "id" program.
     *
     * @param id the id of the program to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the program, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/programs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Program> getProgram(@PathVariable Long id) {
        log.debug("REST request to get Program : {}", id);
        Program program = programRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(program)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /programs/:id : delete the "id" program.
     *
     * @param id the id of the program to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/programs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProgram(@PathVariable Long id) {
        log.debug("REST request to delete Program : {}", id);
        programRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("program", id.toString())).build();
    }

}
