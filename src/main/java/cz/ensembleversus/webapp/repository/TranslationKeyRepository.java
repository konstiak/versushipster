package cz.ensembleversus.webapp.repository;

import cz.ensembleversus.webapp.domain.TranslationKey;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TranslationKey entity.
 */
@SuppressWarnings("unused")
public interface TranslationKeyRepository extends JpaRepository<TranslationKey,Long> {

}
