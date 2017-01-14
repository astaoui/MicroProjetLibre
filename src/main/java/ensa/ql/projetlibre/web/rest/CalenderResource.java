package ensa.ql.projetlibre.web.rest;

import com.codahale.metrics.annotation.Timed;
import ensa.ql.projetlibre.domain.Calender;
import ensa.ql.projetlibre.repository.CalenderRepository;
import ensa.ql.projetlibre.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Calender.
 */
@RestController
@RequestMapping("/api")
public class CalenderResource {

    private final Logger log = LoggerFactory.getLogger(CalenderResource.class);

    @Inject
    private CalenderRepository calenderRepository;

    /**
     * POST  /calenders : Create a new calender.
     *
     * @param calender the calender to create
     * @return the ResponseEntity with status 201 (Created) and with body the new calender, or with status 400 (Bad Request) if the calender has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/calenders")
    @Timed
    public ResponseEntity<Calender> createCalender(@Valid @RequestBody Calender calender) throws URISyntaxException {
        log.debug("REST request to save Calender : {}", calender);
        if (calender.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("calender",
                "idexists", "A new calender cannot already have an ID")).body(null);
        }
        Calender calenderToSave=new Calender();
        calenderToSave=calender;
        calenderToSave.setDateBegining(calender.getProject().getDateBegining());
        calenderToSave.setDateEnding(calender.getProject().getDateEnding());
        Calender result = calenderRepository.save(calenderToSave);
        return ResponseEntity.created(new URI("/api/calenders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("calender", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /calenders : Updates an existing calender.
     *
     * @param calender the calender to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated calender,
     * or with status 400 (Bad Request) if the calender is not valid,
     * or with status 500 (Internal Server Error) if the calender couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/calenders")
    @Timed
    public ResponseEntity<Calender> updateCalender(@Valid @RequestBody Calender calender) throws URISyntaxException {
        log.debug("REST request to update Calender : {}", calender);
        if (calender.getId() == null) {
            return createCalender(calender);
        }
        Calender result = calenderRepository.save(calender);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("calender", calender.getId().toString()))
            .body(result);
    }

    /**
     * GET  /calenders : get all the calenders.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of calenders in body
     */
    @GetMapping("/calenders")
    @Timed
    public List<Calender> getAllCalenders() {
        log.debug("REST request to get all Calenders");
        List<Calender> calenders = calenderRepository.findAll();
        return calenders;
    }

    /**
     * GET  /calenders/:id : get the "id" calender.
     *
     * @param id the id of the calender to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the calender, or with status 404 (Not Found)
     */
    @GetMapping("/calenders/{id}")
    @Timed
    public ResponseEntity<Calender> getCalender(@PathVariable Long id) {
        log.debug("REST request to get Calender : {}", id);
        Calender calender = calenderRepository.findOne(id);
        return Optional.ofNullable(calender)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /calenders/:id : delete the "id" calender.
     *
     * @param id the id of the calender to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/calenders/{id}")
    @Timed
    public ResponseEntity<Void> deleteCalender(@PathVariable Long id) {
        log.debug("REST request to delete Calender : {}", id);
        calenderRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("calender", id.toString())).build();
    }

}
