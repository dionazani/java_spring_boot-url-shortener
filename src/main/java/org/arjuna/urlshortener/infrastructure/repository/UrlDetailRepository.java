package org.arjuna.urlshortener.infrastructure.repository;

import java.util.Optional;

import org.arjuna.urlshortener.infrastructure.entity.UrlDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlDetailRepository extends JpaRepository<UrlDetailEntity, Integer> {

	Optional<UrlDetailEntity> findByUrlId(int urlId);
	Optional<UrlDetailEntity> findByShortUrl(String urlShort);
}
