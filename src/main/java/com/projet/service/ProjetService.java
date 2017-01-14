package com.projet.service;

import com.projet.domain.Projet;
import com.projet.repository.ProjetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Projet.
 */
@Service
@Transactional
public class ProjetService {

    private final Logger log = LoggerFactory.getLogger(ProjetService.class);
    
    @Inject
    private ProjetRepository projetRepository;

    /**
     * Save a projet.
     *
     * @param projet the entity to save
     * @return the persisted entity
     */
    public Projet save(Projet projet) {
        log.debug("Request to save Projet : {}", projet);
        Projet result = projetRepository.save(projet);
        return result;
    }

    /**
     *  Get all the projets.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Projet> findAll(Pageable pageable) {
        log.debug("Request to get all Projets");
        Page<Projet> result = projetRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one projet by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Projet findOne(Long id) {
        log.debug("Request to get Projet : {}", id);
        Projet projet = projetRepository.findOne(id);
        return projet;
    }

    /**
     *  Delete the  projet by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Projet : {}", id);
        projetRepository.delete(id);
    }
}
