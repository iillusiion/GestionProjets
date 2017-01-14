package com.projet.service;

import com.projet.domain.RendezVous;
import com.projet.repository.RendezVousRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing RendezVous.
 */
@Service
@Transactional
public class RendezVousService {

    private final Logger log = LoggerFactory.getLogger(RendezVousService.class);
    
    @Inject
    private RendezVousRepository rendezVousRepository;

    /**
     * Save a rendezVous.
     *
     * @param rendezVous the entity to save
     * @return the persisted entity
     */
    public RendezVous save(RendezVous rendezVous) {
        log.debug("Request to save RendezVous : {}", rendezVous);
        RendezVous result = rendezVousRepository.save(rendezVous);
        return result;
    }

    /**
     *  Get all the rendezVous.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<RendezVous> findAll(Pageable pageable) {
        log.debug("Request to get all RendezVous");
        Page<RendezVous> result = rendezVousRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one rendezVous by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public RendezVous findOne(Long id) {
        log.debug("Request to get RendezVous : {}", id);
        RendezVous rendezVous = rendezVousRepository.findOne(id);
        return rendezVous;
    }

    /**
     *  Delete the  rendezVous by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete RendezVous : {}", id);
        rendezVousRepository.delete(id);
    }
}
