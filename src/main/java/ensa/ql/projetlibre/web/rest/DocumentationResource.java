package ensa.ql.projetlibre.web.rest;

import com.codahale.metrics.annotation.Timed;
import ensa.ql.projetlibre.domain.Documentation;

import ensa.ql.projetlibre.repository.DocumentationRepository;
import ensa.ql.projetlibre.web.rest.util.HeaderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Documentation.
 */
@RestController
@RequestMapping("/api")
public class DocumentationResource {

    private final Logger log = LoggerFactory.getLogger(DocumentationResource.class);
        
    @Inject
    private DocumentationRepository documentationRepository;

    /**
     * POST  /documentations : Create a new documentation.
     *
     * @param documentation the documentation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new documentation, or with status 400 (Bad Request) if the documentation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/documentations")
    @Timed
    public ResponseEntity<Documentation> createDocumentation(@Valid @RequestBody Documentation documentation) throws URISyntaxException {
        log.debug("REST request to save Documentation : {}", documentation);
        if (documentation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("documentation", "idexists", "A new documentation cannot already have an ID")).body(null);
        }
        Documentation result = documentationRepository.save(documentation);
        return ResponseEntity.created(new URI("/api/documentations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("documentation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /documentations : Updates an existing documentation.
     *
     * @param documentation the documentation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated documentation,
     * or with status 400 (Bad Request) if the documentation is not valid,
     * or with status 500 (Internal Server Error) if the documentation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/documentations")
    @Timed
    public ResponseEntity<Documentation> updateDocumentation(@Valid @RequestBody Documentation documentation) throws URISyntaxException {
        log.debug("REST request to update Documentation : {}", documentation);
        if (documentation.getId() == null) {
            return createDocumentation(documentation);
        }
        Documentation result = documentationRepository.save(documentation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("documentation", documentation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /documentations : get all the documentations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of documentations in body
     */
    @GetMapping("/documentations")
    @Timed
    public List<Documentation> getAllDocumentations() {
        log.debug("REST request to get all Documentations");
        List<Documentation> documentations = documentationRepository.findAllWithEagerRelationships();
        return documentations;
    }

    /**
     * GET  /documentations/:id : get the "id" documentation.
     *
     * @param id the id of the documentation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the documentation, or with status 404 (Not Found)
     */
    @GetMapping("/documentations/{id}")
    @Timed
    public ResponseEntity<Documentation> getDocumentation(@PathVariable Long id) {
        log.debug("REST request to get Documentation : {}", id);
        Documentation documentation = documentationRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(documentation)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /documentations/:id : delete the "id" documentation.
     *
     * @param id the id of the documentation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/documentations/{id}")
    @Timed
    public ResponseEntity<Void> deleteDocumentation(@PathVariable Long id) {
        log.debug("REST request to delete Documentation : {}", id);
        documentationRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("documentation", id.toString())).build();
    }

}
