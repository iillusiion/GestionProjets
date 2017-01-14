package com.projet.repository;

import com.projet.domain.Projet;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Projet entity.
 */
@SuppressWarnings("unused")
public interface ProjetRepository extends JpaRepository<Projet,Long> {

}
