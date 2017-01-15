package com.projet.repository;

import com.projet.domain.RendezVous;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Spring Data JPA repository for the RendezVous entity.
 */
@SuppressWarnings("unused")
public interface RendezVousRepository extends JpaRepository<RendezVous,Long> {

    @Query("select rendezVous from RendezVous rendezVous where rendezVous.user.login = ?#{principal.username}")
    List<RendezVous> findByUserIsCurrentUser();

    public Page<RendezVous> findByUserLogin(String currentUserLogin, Pageable pageable);

}
