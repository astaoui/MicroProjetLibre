package ensa.ql.projetlibre.repository;

import ensa.ql.projetlibre.domain.Calender;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Calender entity.
 */
@SuppressWarnings("unused")
public interface CalenderRepository extends JpaRepository<Calender,Long> {

}
