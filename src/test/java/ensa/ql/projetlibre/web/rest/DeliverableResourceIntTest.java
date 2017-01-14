package ensa.ql.projetlibre.web.rest;

import ensa.ql.projetlibre.ProjectMicroServicesApp;

import ensa.ql.projetlibre.domain.Deliverable;
import ensa.ql.projetlibre.repository.DeliverableRepository;

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

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DeliverableResource REST controller.
 *
 * @see DeliverableResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProjectMicroServicesApp.class)
public class DeliverableResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_BEGINING = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_BEGINING = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_ENDING = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ENDING = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private DeliverableRepository deliverableRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDeliverableMockMvc;

    private Deliverable deliverable;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DeliverableResource deliverableResource = new DeliverableResource();
        ReflectionTestUtils.setField(deliverableResource, "deliverableRepository", deliverableRepository);
        this.restDeliverableMockMvc = MockMvcBuilders.standaloneSetup(deliverableResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deliverable createEntity(EntityManager em) {
        Deliverable deliverable = new Deliverable()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .dateBegining(DEFAULT_DATE_BEGINING)
                .dateEnding(DEFAULT_DATE_ENDING);
        return deliverable;
    }

    @Before
    public void initTest() {
        deliverable = createEntity(em);
    }

    @Test
    @Transactional
    public void createDeliverable() throws Exception {
        int databaseSizeBeforeCreate = deliverableRepository.findAll().size();

        // Create the Deliverable

        restDeliverableMockMvc.perform(post("/api/deliverables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deliverable)))
            .andExpect(status().isCreated());

        // Validate the Deliverable in the database
        List<Deliverable> deliverableList = deliverableRepository.findAll();
        assertThat(deliverableList).hasSize(databaseSizeBeforeCreate + 1);
        Deliverable testDeliverable = deliverableList.get(deliverableList.size() - 1);
        assertThat(testDeliverable.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDeliverable.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDeliverable.getDateBegining()).isEqualTo(DEFAULT_DATE_BEGINING);
        assertThat(testDeliverable.getDateEnding()).isEqualTo(DEFAULT_DATE_ENDING);
    }

    @Test
    @Transactional
    public void createDeliverableWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = deliverableRepository.findAll().size();

        // Create the Deliverable with an existing ID
        Deliverable existingDeliverable = new Deliverable();
        existingDeliverable.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeliverableMockMvc.perform(post("/api/deliverables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingDeliverable)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Deliverable> deliverableList = deliverableRepository.findAll();
        assertThat(deliverableList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliverableRepository.findAll().size();
        // set the field null
        deliverable.setName(null);

        // Create the Deliverable, which fails.

        restDeliverableMockMvc.perform(post("/api/deliverables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deliverable)))
            .andExpect(status().isBadRequest());

        List<Deliverable> deliverableList = deliverableRepository.findAll();
        assertThat(deliverableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateBeginingIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliverableRepository.findAll().size();
        // set the field null
        deliverable.setDateBegining(null);

        // Create the Deliverable, which fails.

        restDeliverableMockMvc.perform(post("/api/deliverables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deliverable)))
            .andExpect(status().isBadRequest());

        List<Deliverable> deliverableList = deliverableRepository.findAll();
        assertThat(deliverableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDeliverables() throws Exception {
        // Initialize the database
        deliverableRepository.saveAndFlush(deliverable);

        // Get all the deliverableList
        restDeliverableMockMvc.perform(get("/api/deliverables?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deliverable.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].dateBegining").value(hasItem(DEFAULT_DATE_BEGINING.toString())))
            .andExpect(jsonPath("$.[*].dateEnding").value(hasItem(DEFAULT_DATE_ENDING.toString())));
    }

    @Test
    @Transactional
    public void getDeliverable() throws Exception {
        // Initialize the database
        deliverableRepository.saveAndFlush(deliverable);

        // Get the deliverable
        restDeliverableMockMvc.perform(get("/api/deliverables/{id}", deliverable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(deliverable.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.dateBegining").value(DEFAULT_DATE_BEGINING.toString()))
            .andExpect(jsonPath("$.dateEnding").value(DEFAULT_DATE_ENDING.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDeliverable() throws Exception {
        // Get the deliverable
        restDeliverableMockMvc.perform(get("/api/deliverables/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDeliverable() throws Exception {
        // Initialize the database
        deliverableRepository.saveAndFlush(deliverable);
        int databaseSizeBeforeUpdate = deliverableRepository.findAll().size();

        // Update the deliverable
        Deliverable updatedDeliverable = deliverableRepository.findOne(deliverable.getId());
        updatedDeliverable
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION)
                .dateBegining(UPDATED_DATE_BEGINING)
                .dateEnding(UPDATED_DATE_ENDING);

        restDeliverableMockMvc.perform(put("/api/deliverables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDeliverable)))
            .andExpect(status().isOk());

        // Validate the Deliverable in the database
        List<Deliverable> deliverableList = deliverableRepository.findAll();
        assertThat(deliverableList).hasSize(databaseSizeBeforeUpdate);
        Deliverable testDeliverable = deliverableList.get(deliverableList.size() - 1);
        assertThat(testDeliverable.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDeliverable.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDeliverable.getDateBegining()).isEqualTo(UPDATED_DATE_BEGINING);
        assertThat(testDeliverable.getDateEnding()).isEqualTo(UPDATED_DATE_ENDING);
    }

    @Test
    @Transactional
    public void updateNonExistingDeliverable() throws Exception {
        int databaseSizeBeforeUpdate = deliverableRepository.findAll().size();

        // Create the Deliverable

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDeliverableMockMvc.perform(put("/api/deliverables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deliverable)))
            .andExpect(status().isCreated());

        // Validate the Deliverable in the database
        List<Deliverable> deliverableList = deliverableRepository.findAll();
        assertThat(deliverableList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDeliverable() throws Exception {
        // Initialize the database
        deliverableRepository.saveAndFlush(deliverable);
        int databaseSizeBeforeDelete = deliverableRepository.findAll().size();

        // Get the deliverable
        restDeliverableMockMvc.perform(delete("/api/deliverables/{id}", deliverable.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Deliverable> deliverableList = deliverableRepository.findAll();
        assertThat(deliverableList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
