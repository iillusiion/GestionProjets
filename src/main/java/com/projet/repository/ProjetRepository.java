package com.projet.repository;

import com.projet.domain.Projet;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Spring Data JPA repository for the Projet entity.
 */
@SuppressWarnings("unused")
public interface ProjetRepository extends JpaRepository<Projet,Long> {

    public Page<Projet> findByUserLogin(String currentUserLogin, Pageable pageable);

}
