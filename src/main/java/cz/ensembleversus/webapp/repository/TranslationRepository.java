package cz.ensembleversus.webapp.repository;

import cz.ensembleversus.webapp.domain.Translation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Translation entity.
 */
@SuppressWarnings("unused")
public interface TranslationRepository extends JpaRepository<Translation,Long> {

}
