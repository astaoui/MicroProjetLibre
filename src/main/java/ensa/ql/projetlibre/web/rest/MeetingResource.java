package ensa.ql.projetlibre.web.rest;

import com.codahale.metrics.annotation.Timed;
import ensa.ql.projetlibre.domain.Meeting;

import ensa.ql.projetlibre.repository.MeetingRepository;
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
 * REST controller for managing Meeting.
 */
@RestController
@RequestMapping("/api")
public class MeetingResource {

    private final Logger log = LoggerFactory.getLogger(MeetingResource.class);
        
    @Inject
    private MeetingRepository meetingRepository;

    /**
     * POST  /meetings : Create a new meeting.
     *
     * @param meeting the meeting to create
     * @return the ResponseEntity with status 201 (Created) and with body the new meeting, or with status 400 (Bad Request) if the meeting has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/meetings")
    @Timed
    public ResponseEntity<Meeting> createMeeting(@Valid @RequestBody Meeting meeting) throws URISyntaxException {
        log.debug("REST request to save Meeting : {}", meeting);
        if (meeting.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("meeting", "idexists", "A new meeting cannot already have an ID")).body(null);
        }
        Meeting result = meetingRepository.save(meeting);
        return ResponseEntity.created(new URI("/api/meetings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("meeting", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /meetings : Updates an existing meeting.
     *
     * @param meeting the meeting to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated meeting,
     * or with status 400 (Bad Request) if the meeting is not valid,
     * or with status 500 (Internal Server Error) if the meeting couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/meetings")
    @Timed
    public ResponseEntity<Meeting> updateMeeting(@Valid @RequestBody Meeting meeting) throws URISyntaxException {
        log.debug("REST request to update Meeting : {}", meeting);
        if (meeting.getId() == null) {
            return createMeeting(meeting);
        }
        Meeting result = meetingRepository.save(meeting);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("meeting", meeting.getId().toString()))
            .body(result);
    }

    /**
     * GET  /meetings : get all the meetings.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of meetings in body
     */
    @GetMapping("/meetings")
    @Timed
    public List<Meeting> getAllMeetings() {
        log.debug("REST request to get all Meetings");
        List<Meeting> meetings = meetingRepository.findAll();
        return meetings;
    }

    /**
     * GET  /meetings/:id : get the "id" meeting.
     *
     * @param id the id of the meeting to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the meeting, or with status 404 (Not Found)
     */
    @GetMapping("/meetings/{id}")
    @Timed
    public ResponseEntity<Meeting> getMeeting(@PathVariable Long id) {
        log.debug("REST request to get Meeting : {}", id);
        Meeting meeting = meetingRepository.findOne(id);
        return Optional.ofNullable(meeting)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /meetings/:id : delete the "id" meeting.
     *
     * @param id the id of the meeting to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/meetings/{id}")
    @Timed
    public ResponseEntity<Void> deleteMeeting(@PathVariable Long id) {
        log.debug("REST request to delete Meeting : {}", id);
        meetingRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("meeting", id.toString())).build();
    }

}
