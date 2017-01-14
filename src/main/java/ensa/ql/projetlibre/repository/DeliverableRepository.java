package ensa.ql.projetlibre.repository;

import ensa.ql.projetlibre.domain.Deliverable;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Deliverable entity.
 */
@SuppressWarnings("unused")
public interface DeliverableRepository extends JpaRepository<Deliverable,Long> {

}
