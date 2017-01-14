package com.projet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.projet.domain.Projet;
import com.projet.service.ProjetService;
import com.projet.web.rest.util.HeaderUtil;
import com.projet.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Projet.
 */
@RestController
@RequestMapping("/api")
public class ProjetResource {

    private final Logger log = LoggerFactory.getLogger(ProjetResource.class);
        
    @Inject
    private ProjetService projetService;

    /**
     * POST  /projets : Create a new projet.
     *
     * @param projet the projet to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projet, or with status 400 (Bad Request) if the projet has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/projets")
    @Timed
    public ResponseEntity<Projet> createProjet(@Valid @RequestBody Projet projet) throws URISyntaxException {
        log.debug("REST request to save Projet : {}", projet);
        if (projet.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("projet", "idexists", "A new projet cannot already have an ID")).body(null);
        }
        Projet result = projetService.save(projet);
        return ResponseEntity.created(new URI("/api/projets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("projet", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /projets : Updates an existing projet.
     *
     * @param projet the projet to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projet,
     * or with status 400 (Bad Request) if the projet is not valid,
     * or with status 500 (Internal Server Error) if the projet couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/projets")
    @Timed
    public ResponseEntity<Projet> updateProjet(@Valid @RequestBody Projet projet) throws URISyntaxException {
        log.debug("REST request to update Projet : {}", projet);
        if (projet.getId() == null) {
            return createProjet(projet);
        }
        Projet result = projetService.save(projet);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("projet", projet.getId().toString()))
            .body(result);
    }

    /**
     * GET  /projets : get all the projets.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of projets in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/projets")
    @Timed
    public ResponseEntity<List<Projet>> getAllProjets(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Projets");
        Page<Projet> page = projetService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/projets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /projets/:id : get the "id" projet.
     *
     * @param id the id of the projet to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projet, or with status 404 (Not Found)
     */
    @GetMapping("/projets/{id}")
    @Timed
    public ResponseEntity<Projet> getProjet(@PathVariable Long id) {
        log.debug("REST request to get Projet : {}", id);
        Projet projet = projetService.findOne(id);
        return Optional.ofNullable(projet)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /projets/:id : delete the "id" projet.
     *
     * @param id the id of the projet to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/projets/{id}")
    @Timed
    public ResponseEntity<Void> deleteProjet(@PathVariable Long id) {
        log.debug("REST request to delete Projet : {}", id);
        projetService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("projet", id.toString())).build();
    }

}
