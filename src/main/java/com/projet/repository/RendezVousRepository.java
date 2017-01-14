package com.projet.repository;

import com.projet.domain.RendezVous;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the RendezVous entity.
 */
@SuppressWarnings("unused")
public interface RendezVousRepository extends JpaRepository<RendezVous,Long> {

    @Query("select rendezVous from RendezVous rendezVous where rendezVous.user.login = ?#{principal.username}")
    List<RendezVous> findByUserIsCurrentUser();

}
