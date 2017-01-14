package ensa.ql.projetlibre.web.rest;

import com.codahale.metrics.annotation.Timed;
import ensa.ql.projetlibre.domain.Deliverable;

import ensa.ql.projetlibre.repository.DeliverableRepository;
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
 * REST controller for managing Deliverable.
 */
@RestController
@RequestMapping("/api")
public class DeliverableResource {

    private final Logger log = LoggerFactory.getLogger(DeliverableResource.class);
        
    @Inject
    private DeliverableRepository deliverableRepository;

    /**
     * POST  /deliverables : Create a new deliverable.
     *
     * @param deliverable the deliverable to create
     * @return the ResponseEntity with status 201 (Created) and with body the new deliverable, or with status 400 (Bad Request) if the deliverable has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/deliverables")
    @Timed
    public ResponseEntity<Deliverable> createDeliverable(@Valid @RequestBody Deliverable deliverable) throws URISyntaxException {
        log.debug("REST request to save Deliverable : {}", deliverable);
        if (deliverable.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("deliverable", "idexists", "A new deliverable cannot already have an ID")).body(null);
        }
        Deliverable result = deliverableRepository.save(deliverable);
        return ResponseEntity.created(new URI("/api/deliverables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("deliverable", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /deliverables : Updates an existing deliverable.
     *
     * @param deliverable the deliverable to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated deliverable,
     * or with status 400 (Bad Request) if the deliverable is not valid,
     * or with status 500 (Internal Server Error) if the deliverable couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/deliverables")
    @Timed
    public ResponseEntity<Deliverable> updateDeliverable(@Valid @RequestBody Deliverable deliverable) throws URISyntaxException {
        log.debug("REST request to update Deliverable : {}", deliverable);
        if (deliverable.getId() == null) {
            return createDeliverable(deliverable);
        }
        Deliverable result = deliverableRepository.save(deliverable);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("deliverable", deliverable.getId().toString()))
            .body(result);
    }

    /**
     * GET  /deliverables : get all the deliverables.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of deliverables in body
     */
    @GetMapping("/deliverables")
    @Timed
    public List<Deliverable> getAllDeliverables() {
        log.debug("REST request to get all Deliverables");
        List<Deliverable> deliverables = deliverableRepository.findAll();
        return deliverables;
    }

    /**
     * GET  /deliverables/:id : get the "id" deliverable.
     *
     * @param id the id of the deliverable to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the deliverable, or with status 404 (Not Found)
     */
    @GetMapping("/deliverables/{id}")
    @Timed
    public ResponseEntity<Deliverable> getDeliverable(@PathVariable Long id) {
        log.debug("REST request to get Deliverable : {}", id);
        Deliverable deliverable = deliverableRepository.findOne(id);
        return Optional.ofNullable(deliverable)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /deliverables/:id : delete the "id" deliverable.
     *
     * @param id the id of the deliverable to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/deliverables/{id}")
    @Timed
    public ResponseEntity<Void> deleteDeliverable(@PathVariable Long id) {
        log.debug("REST request to delete Deliverable : {}", id);
        deliverableRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("deliverable", id.toString())).build();
    }

}
