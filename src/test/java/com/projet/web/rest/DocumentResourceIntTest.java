package com.projet.web.rest;

import com.projet.GestionProjetApp;

import com.projet.domain.Document;
import com.projet.repository.DocumentRepository;
import com.projet.service.DocumentService;

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
 * Test class for the DocumentResource REST controller.
 *
 * @see DocumentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GestionProjetApp.class)
public class DocumentResourceIntTest {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DEPOSITION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEPOSITION = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_TYPE_DOCUMENT = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_DOCUMENT = "BBBBBBBBBB";

    @Inject
    private DocumentRepository documentRepository;

    @Inject
    private DocumentService documentService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDocumentMockMvc;

    private Document document;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DocumentResource documentResource = new DocumentResource();
        ReflectionTestUtils.setField(documentResource, "documentService", documentService);
        this.restDocumentMockMvc = MockMvcBuilders.standaloneSetup(documentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Document createEntity(EntityManager em) {
        Document document = new Document()
                .libelle(DEFAULT_LIBELLE)
                .description(DEFAULT_DESCRIPTION)
                .dateDeposition(DEFAULT_DATE_DEPOSITION)
                .typeDocument(DEFAULT_TYPE_DOCUMENT);
        return document;
    }

    @Before
    public void initTest() {
        document = createEntity(em);
    }

    @Test
    @Transactional
    public void createDocument() throws Exception {
        int databaseSizeBeforeCreate = documentRepository.findAll().size();

        // Create the Document

        restDocumentMockMvc.perform(post("/api/documents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(document)))
                .andExpect(status().isCreated());

        // Validate the Document in the database
        List<Document> documents = documentRepository.findAll();
        assertThat(documents).hasSize(databaseSizeBeforeCreate + 1);
        Document testDocument = documents.get(documents.size() - 1);
        assertThat(testDocument.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testDocument.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDocument.getDateDeposition()).isEqualTo(DEFAULT_DATE_DEPOSITION);
        assertThat(testDocument.getTypeDocument()).isEqualTo(DEFAULT_TYPE_DOCUMENT);
    }

    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentRepository.findAll().size();
        // set the field null
        document.setLibelle(null);

        // Create the Document, which fails.

        restDocumentMockMvc.perform(post("/api/documents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(document)))
                .andExpect(status().isBadRequest());

        List<Document> documents = documentRepository.findAll();
        assertThat(documents).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentRepository.findAll().size();
        // set the field null
        document.setDescription(null);

        // Create the Document, which fails.

        restDocumentMockMvc.perform(post("/api/documents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(document)))
                .andExpect(status().isBadRequest());

        List<Document> documents = documentRepository.findAll();
        assertThat(documents).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateDepositionIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentRepository.findAll().size();
        // set the field null
        document.setDateDeposition(null);

        // Create the Document, which fails.

        restDocumentMockMvc.perform(post("/api/documents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(document)))
                .andExpect(status().isBadRequest());

        List<Document> documents = documentRepository.findAll();
        assertThat(documents).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeDocumentIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentRepository.findAll().size();
        // set the field null
        document.setTypeDocument(null);

        // Create the Document, which fails.

        restDocumentMockMvc.perform(post("/api/documents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(document)))
                .andExpect(status().isBadRequest());

        List<Document> documents = documentRepository.findAll();
        assertThat(documents).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDocuments() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documents
        restDocumentMockMvc.perform(get("/api/documents?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(document.getId().intValue())))
                .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].dateDeposition").value(hasItem(DEFAULT_DATE_DEPOSITION.toString())))
                .andExpect(jsonPath("$.[*].typeDocument").value(hasItem(DEFAULT_TYPE_DOCUMENT.toString())));
    }

    @Test
    @Transactional
    public void getDocument() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get the document
        restDocumentMockMvc.perform(get("/api/documents/{id}", document.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(document.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.dateDeposition").value(DEFAULT_DATE_DEPOSITION.toString()))
            .andExpect(jsonPath("$.typeDocument").value(DEFAULT_TYPE_DOCUMENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDocument() throws Exception {
        // Get the document
        restDocumentMockMvc.perform(get("/api/documents/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDocument() throws Exception {
        // Initialize the database
        documentService.save(document);

        int databaseSizeBeforeUpdate = documentRepository.findAll().size();

        // Update the document
        Document updatedDocument = documentRepository.findOne(document.getId());
        updatedDocument
                .libelle(UPDATED_LIBELLE)
                .description(UPDATED_DESCRIPTION)
                .dateDeposition(UPDATED_DATE_DEPOSITION)
                .typeDocument(UPDATED_TYPE_DOCUMENT);

        restDocumentMockMvc.perform(put("/api/documents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDocument)))
                .andExpect(status().isOk());

        // Validate the Document in the database
        List<Document> documents = documentRepository.findAll();
        assertThat(documents).hasSize(databaseSizeBeforeUpdate);
        Document testDocument = documents.get(documents.size() - 1);
        assertThat(testDocument.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testDocument.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDocument.getDateDeposition()).isEqualTo(UPDATED_DATE_DEPOSITION);
        assertThat(testDocument.getTypeDocument()).isEqualTo(UPDATED_TYPE_DOCUMENT);
    }

    @Test
    @Transactional
    public void deleteDocument() throws Exception {
        // Initialize the database
        documentService.save(document);

        int databaseSizeBeforeDelete = documentRepository.findAll().size();

        // Get the document
        restDocumentMockMvc.perform(delete("/api/documents/{id}", document.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Document> documents = documentRepository.findAll();
        assertThat(documents).hasSize(databaseSizeBeforeDelete - 1);
    }
}
