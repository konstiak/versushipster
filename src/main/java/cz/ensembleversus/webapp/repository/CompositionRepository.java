package cz.ensembleversus.webapp.repository;

import cz.ensembleversus.webapp.domain.Composition;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Composition entity.
 */
@SuppressWarnings("unused")
public interface CompositionRepository extends JpaRepository<Composition,Long> {

}
