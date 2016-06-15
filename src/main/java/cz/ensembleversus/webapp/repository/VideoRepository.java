package cz.ensembleversus.webapp.repository;

import cz.ensembleversus.webapp.domain.Video;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Video entity.
 */
@SuppressWarnings("unused")
public interface VideoRepository extends JpaRepository<Video,Long> {

}
