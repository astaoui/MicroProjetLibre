package ensa.ql.projetlibre.web.rest;

import ensa.ql.projetlibre.ProjectMicroServicesApp;

import ensa.ql.projetlibre.domain.Documentation;
import ensa.ql.projetlibre.repository.DocumentationRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DocumentationResource REST controller.
 *
 * @see DocumentationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProjectMicroServicesApp.class)
public class DocumentationResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_CONTENT_TYPE = "image/png";

    @Inject
    private DocumentationRepository documentationRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDocumentationMockMvc;

    private Documentation documentation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DocumentationResource documentationResource = new DocumentationResource();
        ReflectionTestUtils.setField(documentationResource, "documentationRepository", documentationRepository);
        this.restDocumentationMockMvc = MockMvcBuilders.standaloneSetup(documentationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Documentation createEntity(EntityManager em) {
        Documentation documentation = new Documentation()
                .title(DEFAULT_TITLE)
                .description(DEFAULT_DESCRIPTION)
                .file(DEFAULT_FILE)
                .fileContentType(DEFAULT_FILE_CONTENT_TYPE);
        return documentation;
    }

    @Before
    public void initTest() {
        documentation = createEntity(em);
    }

    @Test
    @Transactional
    public void createDocumentation() throws Exception {
        int databaseSizeBeforeCreate = documentationRepository.findAll().size();

        // Create the Documentation

        restDocumentationMockMvc.perform(post("/api/documentations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentation)))
            .andExpect(status().isCreated());

        // Validate the Documentation in the database
        List<Documentation> documentationList = documentationRepository.findAll();
        assertThat(documentationList).hasSize(databaseSizeBeforeCreate + 1);
        Documentation testDocumentation = documentationList.get(documentationList.size() - 1);
        assertThat(testDocumentation.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testDocumentation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDocumentation.getFile()).isEqualTo(DEFAULT_FILE);
        assertThat(testDocumentation.getFileContentType()).isEqualTo(DEFAULT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createDocumentationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = documentationRepository.findAll().size();

        // Create the Documentation with an existing ID
        Documentation existingDocumentation = new Documentation();
        existingDocumentation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentationMockMvc.perform(post("/api/documentations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingDocumentation)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Documentation> documentationList = documentationRepository.findAll();
        assertThat(documentationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentationRepository.findAll().size();
        // set the field null
        documentation.setTitle(null);

        // Create the Documentation, which fails.

        restDocumentationMockMvc.perform(post("/api/documentations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentation)))
            .andExpect(status().isBadRequest());

        List<Documentation> documentationList = documentationRepository.findAll();
        assertThat(documentationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDocumentations() throws Exception {
        // Initialize the database
        documentationRepository.saveAndFlush(documentation);

        // Get all the documentationList
        restDocumentationMockMvc.perform(get("/api/documentations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentation.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].file").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE))));
    }

    @Test
    @Transactional
    public void getDocumentation() throws Exception {
        // Initialize the database
        documentationRepository.saveAndFlush(documentation);

        // Get the documentation
        restDocumentationMockMvc.perform(get("/api/documentations/{id}", documentation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(documentation.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.fileContentType").value(DEFAULT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.file").value(Base64Utils.encodeToString(DEFAULT_FILE)));
    }

    @Test
    @Transactional
    public void getNonExistingDocumentation() throws Exception {
        // Get the documentation
        restDocumentationMockMvc.perform(get("/api/documentations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDocumentation() throws Exception {
        // Initialize the database
        documentationRepository.saveAndFlush(documentation);
        int databaseSizeBeforeUpdate = documentationRepository.findAll().size();

        // Update the documentation
        Documentation updatedDocumentation = documentationRepository.findOne(documentation.getId());
        updatedDocumentation
                .title(UPDATED_TITLE)
                .description(UPDATED_DESCRIPTION)
                .file(UPDATED_FILE)
                .fileContentType(UPDATED_FILE_CONTENT_TYPE);

        restDocumentationMockMvc.perform(put("/api/documentations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDocumentation)))
            .andExpect(status().isOk());

        // Validate the Documentation in the database
        List<Documentation> documentationList = documentationRepository.findAll();
        assertThat(documentationList).hasSize(databaseSizeBeforeUpdate);
        Documentation testDocumentation = documentationList.get(documentationList.size() - 1);
        assertThat(testDocumentation.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testDocumentation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDocumentation.getFile()).isEqualTo(UPDATED_FILE);
        assertThat(testDocumentation.getFileContentType()).isEqualTo(UPDATED_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingDocumentation() throws Exception {
        int databaseSizeBeforeUpdate = documentationRepository.findAll().size();

        // Create the Documentation

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDocumentationMockMvc.perform(put("/api/documentations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentation)))
            .andExpect(status().isCreated());

        // Validate the Documentation in the database
        List<Documentation> documentationList = documentationRepository.findAll();
        assertThat(documentationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDocumentation() throws Exception {
        // Initialize the database
        documentationRepository.saveAndFlush(documentation);
        int databaseSizeBeforeDelete = documentationRepository.findAll().size();

        // Get the documentation
        restDocumentationMockMvc.perform(delete("/api/documentations/{id}", documentation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Documentation> documentationList = documentationRepository.findAll();
        assertThat(documentationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
