package ensa.ql.projetlibre.repository;

import ensa.ql.projetlibre.domain.Documentation;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Documentation entity.
 */
@SuppressWarnings("unused")
public interface DocumentationRepository extends JpaRepository<Documentation,Long> {

    @Query("select distinct documentation from Documentation documentation left join fetch documentation.tags")
    List<Documentation> findAllWithEagerRelationships();

    @Query("select documentation from Documentation documentation left join fetch documentation.tags where documentation.id =:id")
    Documentation findOneWithEagerRelationships(@Param("id") Long id);

}
