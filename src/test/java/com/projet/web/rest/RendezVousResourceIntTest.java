package com.projet.web.rest;

import com.projet.GestionProjetApp;

import com.projet.domain.RendezVous;
import com.projet.domain.User;
import com.projet.repository.RendezVousRepository;
import com.projet.service.RendezVousService;

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
 * Test class for the RendezVousResource REST controller.
 *
 * @see RendezVousResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GestionProjetApp.class)
public class RendezVousResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ORDRE_DU_JOUR = "AAAAAAAAAA";
    private static final String UPDATED_ORDRE_DU_JOUR = "BBBBBBBBBB";

    @Inject
    private RendezVousRepository rendezVousRepository;

    @Inject
    private RendezVousService rendezVousService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restRendezVousMockMvc;

    private RendezVous rendezVous;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RendezVousResource rendezVousResource = new RendezVousResource();
        ReflectionTestUtils.setField(rendezVousResource, "rendezVousService", rendezVousService);
        this.restRendezVousMockMvc = MockMvcBuilders.standaloneSetup(rendezVousResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RendezVous createEntity(EntityManager em) {
        RendezVous rendezVous = new RendezVous()
                .date(DEFAULT_DATE)
                .description(DEFAULT_DESCRIPTION)
                .ordreDuJour(DEFAULT_ORDRE_DU_JOUR);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        rendezVous.setUser(user);
        return rendezVous;
    }

    @Before
    public void initTest() {
        rendezVous = createEntity(em);
    }

    @Test
    @Transactional
    public void createRendezVous() throws Exception {
        int databaseSizeBeforeCreate = rendezVousRepository.findAll().size();

        // Create the RendezVous

        restRendezVousMockMvc.perform(post("/api/rendez-vous")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(rendezVous)))
                .andExpect(status().isCreated());

        // Validate the RendezVous in the database
        List<RendezVous> rendezVous = rendezVousRepository.findAll();
        assertThat(rendezVous).hasSize(databaseSizeBeforeCreate + 1);
        RendezVous testRendezVous = rendezVous.get(rendezVous.size() - 1);
        assertThat(testRendezVous.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testRendezVous.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRendezVous.getOrdreDuJour()).isEqualTo(DEFAULT_ORDRE_DU_JOUR);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = rendezVousRepository.findAll().size();
        // set the field null
        rendezVous.setDate(null);

        // Create the RendezVous, which fails.

        restRendezVousMockMvc.perform(post("/api/rendez-vous")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(rendezVous)))
                .andExpect(status().isBadRequest());

        List<RendezVous> rendezVous = rendezVousRepository.findAll();
        assertThat(rendezVous).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = rendezVousRepository.findAll().size();
        // set the field null
        rendezVous.setDescription(null);

        // Create the RendezVous, which fails.

        restRendezVousMockMvc.perform(post("/api/rendez-vous")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(rendezVous)))
                .andExpect(status().isBadRequest());

        List<RendezVous> rendezVous = rendezVousRepository.findAll();
        assertThat(rendezVous).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOrdreDuJourIsRequired() throws Exception {
        int databaseSizeBeforeTest = rendezVousRepository.findAll().size();
        // set the field null
        rendezVous.setOrdreDuJour(null);

        // Create the RendezVous, which fails.

        restRendezVousMockMvc.perform(post("/api/rendez-vous")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(rendezVous)))
                .andExpect(status().isBadRequest());

        List<RendezVous> rendezVous = rendezVousRepository.findAll();
        assertThat(rendezVous).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRendezVous() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get all the rendezVous
        restRendezVousMockMvc.perform(get("/api/rendez-vous?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(rendezVous.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].ordreDuJour").value(hasItem(DEFAULT_ORDRE_DU_JOUR.toString())));
    }

    @Test
    @Transactional
    public void getRendezVous() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get the rendezVous
        restRendezVousMockMvc.perform(get("/api/rendez-vous/{id}", rendezVous.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rendezVous.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.ordreDuJour").value(DEFAULT_ORDRE_DU_JOUR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRendezVous() throws Exception {
        // Get the rendezVous
        restRendezVousMockMvc.perform(get("/api/rendez-vous/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRendezVous() throws Exception {
        // Initialize the database
        rendezVousService.save(rendezVous);

        int databaseSizeBeforeUpdate = rendezVousRepository.findAll().size();

        // Update the rendezVous
        RendezVous updatedRendezVous = rendezVousRepository.findOne(rendezVous.getId());
        updatedRendezVous
                .date(UPDATED_DATE)
                .description(UPDATED_DESCRIPTION)
                .ordreDuJour(UPDATED_ORDRE_DU_JOUR);

        restRendezVousMockMvc.perform(put("/api/rendez-vous")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRendezVous)))
                .andExpect(status().isOk());

        // Validate the RendezVous in the database
        List<RendezVous> rendezVous = rendezVousRepository.findAll();
        assertThat(rendezVous).hasSize(databaseSizeBeforeUpdate);
        RendezVous testRendezVous = rendezVous.get(rendezVous.size() - 1);
        assertThat(testRendezVous.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testRendezVous.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRendezVous.getOrdreDuJour()).isEqualTo(UPDATED_ORDRE_DU_JOUR);
    }

    @Test
    @Transactional
    public void deleteRendezVous() throws Exception {
        // Initialize the database
        rendezVousService.save(rendezVous);

        int databaseSizeBeforeDelete = rendezVousRepository.findAll().size();

        // Get the rendezVous
        restRendezVousMockMvc.perform(delete("/api/rendez-vous/{id}", rendezVous.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<RendezVous> rendezVous = rendezVousRepository.findAll();
        assertThat(rendezVous).hasSize(databaseSizeBeforeDelete - 1);
    }
}
