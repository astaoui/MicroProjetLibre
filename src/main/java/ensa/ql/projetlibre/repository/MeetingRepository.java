package ensa.ql.projetlibre.repository;

import ensa.ql.projetlibre.domain.Meeting;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Meeting entity.
 */
@SuppressWarnings("unused")
public interface MeetingRepository extends JpaRepository<Meeting,Long> {

}
