package com.projet.repository;

import com.projet.domain.Document;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Document entity.
 */
@SuppressWarnings("unused")
public interface DocumentRepository extends JpaRepository<Document,Long> {

}
