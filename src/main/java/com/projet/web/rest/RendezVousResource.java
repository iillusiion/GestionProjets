package com.projet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.projet.domain.RendezVous;
import com.projet.service.RendezVousService;
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
 * REST controller for managing RendezVous.
 */
@RestController
@RequestMapping("/api")
public class RendezVousResource {

    private final Logger log = LoggerFactory.getLogger(RendezVousResource.class);
        
    @Inject
    private RendezVousService rendezVousService;

    /**
     * POST  /rendez-vous : Create a new rendezVous.
     *
     * @param rendezVous the rendezVous to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rendezVous, or with status 400 (Bad Request) if the rendezVous has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rendez-vous")
    @Timed
    public ResponseEntity<RendezVous> createRendezVous(@Valid @RequestBody RendezVous rendezVous) throws URISyntaxException {
        log.debug("REST request to save RendezVous : {}", rendezVous);
        if (rendezVous.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("rendezVous", "idexists", "A new rendezVous cannot already have an ID")).body(null);
        }
        RendezVous result = rendezVousService.save(rendezVous);
        return ResponseEntity.created(new URI("/api/rendez-vous/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("rendezVous", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rendez-vous : Updates an existing rendezVous.
     *
     * @param rendezVous the rendezVous to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rendezVous,
     * or with status 400 (Bad Request) if the rendezVous is not valid,
     * or with status 500 (Internal Server Error) if the rendezVous couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rendez-vous")
    @Timed
    public ResponseEntity<RendezVous> updateRendezVous(@Valid @RequestBody RendezVous rendezVous) throws URISyntaxException {
        log.debug("REST request to update RendezVous : {}", rendezVous);
        if (rendezVous.getId() == null) {
            return createRendezVous(rendezVous);
        }
        RendezVous result = rendezVousService.save(rendezVous);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("rendezVous", rendezVous.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rendez-vous : get all the rendezVous.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of rendezVous in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/rendez-vous")
    @Timed
    public ResponseEntity<List<RendezVous>> getAllRendezVous(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of RendezVous");
        Page<RendezVous> page = rendezVousService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/rendez-vous");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /rendez-vous/:id : get the "id" rendezVous.
     *
     * @param id the id of the rendezVous to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rendezVous, or with status 404 (Not Found)
     */
    @GetMapping("/rendez-vous/{id}")
    @Timed
    public ResponseEntity<RendezVous> getRendezVous(@PathVariable Long id) {
        log.debug("REST request to get RendezVous : {}", id);
        RendezVous rendezVous = rendezVousService.findOne(id);
        return Optional.ofNullable(rendezVous)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /rendez-vous/:id : delete the "id" rendezVous.
     *
     * @param id the id of the rendezVous to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rendez-vous/{id}")
    @Timed
    public ResponseEntity<Void> deleteRendezVous(@PathVariable Long id) {
        log.debug("REST request to delete RendezVous : {}", id);
        rendezVousService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("rendezVous", id.toString())).build();
    }

}
