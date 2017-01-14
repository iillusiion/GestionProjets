package com.projet.web.rest;

import com.projet.GestionProjetApp;

import com.projet.domain.Projet;
import com.projet.repository.ProjetRepository;
import com.projet.service.ProjetService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProjetResource REST controller.
 *
 * @see ProjetResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GestionProjetApp.class)
public class ProjetResourceIntTest {

    private static final String DEFAULT_LIBELLE_PROJET = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_PROJET = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE_PROJET = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_PROJET = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private ProjetRepository projetRepository;

    @Inject
    private ProjetService projetService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restProjetMockMvc;

    private Projet projet;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProjetResource projetResource = new ProjetResource();
        ReflectionTestUtils.setField(projetResource, "projetService", projetService);
        this.restProjetMockMvc = MockMvcBuilders.standaloneSetup(projetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Projet createEntity(EntityManager em) {
        Projet projet = new Projet()
                .libelleProjet(DEFAULT_LIBELLE_PROJET)
                .typeProjet(DEFAULT_TYPE_PROJET)
                .dateDebut(DEFAULT_DATE_DEBUT)
                .dateFin(DEFAULT_DATE_FIN);
        return projet;
    }

    @Before
    public void initTest() {
        projet = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjet() throws Exception {
        int databaseSizeBeforeCreate = projetRepository.findAll().size();

        // Create the Projet

        restProjetMockMvc.perform(post("/api/projets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projet)))
                .andExpect(status().isCreated());

        // Validate the Projet in the database
        List<Projet> projets = projetRepository.findAll();
        assertThat(projets).hasSize(databaseSizeBeforeCreate + 1);
        Projet testProjet = projets.get(projets.size() - 1);
        assertThat(testProjet.getLibelleProjet()).isEqualTo(DEFAULT_LIBELLE_PROJET);
        assertThat(testProjet.getTypeProjet()).isEqualTo(DEFAULT_TYPE_PROJET);
        assertThat(testProjet.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testProjet.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
    }

    @Test
    @Transactional
    public void checkLibelleProjetIsRequired() throws Exception {
        int databaseSizeBeforeTest = projetRepository.findAll().size();
        // set the field null
        projet.setLibelleProjet(null);

        // Create the Projet, which fails.

        restProjetMockMvc.perform(post("/api/projets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projet)))
                .andExpect(status().isBadRequest());

        List<Projet> projets = projetRepository.findAll();
        assertThat(projets).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeProjetIsRequired() throws Exception {
        int databaseSizeBeforeTest = projetRepository.findAll().size();
        // set the field null
        projet.setTypeProjet(null);

        // Create the Projet, which fails.

        restProjetMockMvc.perform(post("/api/projets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projet)))
                .andExpect(status().isBadRequest());

        List<Projet> projets = projetRepository.findAll();
        assertThat(projets).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateDebutIsRequired() throws Exception {
        int databaseSizeBeforeTest = projetRepository.findAll().size();
        // set the field null
        projet.setDateDebut(null);

        // Create the Projet, which fails.

        restProjetMockMvc.perform(post("/api/projets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projet)))
                .andExpect(status().isBadRequest());

        List<Projet> projets = projetRepository.findAll();
        assertThat(projets).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProjets() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get all the projets
        restProjetMockMvc.perform(get("/api/projets?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(projet.getId().intValue())))
                .andExpect(jsonPath("$.[*].libelleProjet").value(hasItem(DEFAULT_LIBELLE_PROJET.toString())))
                .andExpect(jsonPath("$.[*].typeProjet").value(hasItem(DEFAULT_TYPE_PROJET.toString())))
                .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
                .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())));
    }

    @Test
    @Transactional
    public void getProjet() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get the projet
        restProjetMockMvc.perform(get("/api/projets/{id}", projet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projet.getId().intValue()))
            .andExpect(jsonPath("$.libelleProjet").value(DEFAULT_LIBELLE_PROJET.toString()))
            .andExpect(jsonPath("$.typeProjet").value(DEFAULT_TYPE_PROJET.toString()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProjet() throws Exception {
        // Get the projet
        restProjetMockMvc.perform(get("/api/projets/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjet() throws Exception {
        // Initialize the database
        projetService.save(projet);

        int databaseSizeBeforeUpdate = projetRepository.findAll().size();

        // Update the projet
        Projet updatedProjet = projetRepository.findOne(projet.getId());
        updatedProjet
                .libelleProjet(UPDATED_LIBELLE_PROJET)
                .typeProjet(UPDATED_TYPE_PROJET)
                .dateDebut(UPDATED_DATE_DEBUT)
                .dateFin(UPDATED_DATE_FIN);

        restProjetMockMvc.perform(put("/api/projets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProjet)))
                .andExpect(status().isOk());

        // Validate the Projet in the database
        List<Projet> projets = projetRepository.findAll();
        assertThat(projets).hasSize(databaseSizeBeforeUpdate);
        Projet testProjet = projets.get(projets.size() - 1);
        assertThat(testProjet.getLibelleProjet()).isEqualTo(UPDATED_LIBELLE_PROJET);
        assertThat(testProjet.getTypeProjet()).isEqualTo(UPDATED_TYPE_PROJET);
        assertThat(testProjet.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testProjet.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    public void deleteProjet() throws Exception {
        // Initialize the database
        projetService.save(projet);

        int databaseSizeBeforeDelete = projetRepository.findAll().size();

        // Get the projet
        restProjetMockMvc.perform(delete("/api/projets/{id}", projet.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Projet> projets = projetRepository.findAll();
        assertThat(projets).hasSize(databaseSizeBeforeDelete - 1);
    }
}
