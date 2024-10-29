package org.arjuna.urlshortener.infrastructure.repository;

import org.arjuna.urlshortener.infrastructure.entity.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends JpaRepository<UrlEntity, Integer> {

}
