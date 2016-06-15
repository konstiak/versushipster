package cz.ensembleversus.webapp.repository;

import cz.ensembleversus.webapp.domain.Composer;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Composer entity.
 */
@SuppressWarnings("unused")
public interface ComposerRepository extends JpaRepository<Composer,Long> {

}
