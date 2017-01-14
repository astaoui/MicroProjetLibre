package ensa.ql.projetlibre.web.rest;

import ensa.ql.projetlibre.ProjectMicroServicesApp;

import ensa.ql.projetlibre.domain.Calender;
import ensa.ql.projetlibre.repository.CalenderRepository;

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
 * Test class for the CalenderResource REST controller.
 *
 * @see CalenderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProjectMicroServicesApp.class)
public class CalenderResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_BEGINING = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_BEGINING = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_ENDING = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ENDING = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private CalenderRepository calenderRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCalenderMockMvc;

    private Calender calender;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CalenderResource calenderResource = new CalenderResource();
        ReflectionTestUtils.setField(calenderResource, "calenderRepository", calenderRepository);
        this.restCalenderMockMvc = MockMvcBuilders.standaloneSetup(calenderResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Calender createEntity(EntityManager em) {
        Calender calender = new Calender()
                .title(DEFAULT_TITLE)
                .dateBegining(DEFAULT_DATE_BEGINING)
                .dateEnding(DEFAULT_DATE_ENDING);
        return calender;
    }

    @Before
    public void initTest() {
        calender = createEntity(em);
    }

    @Test
    @Transactional
    public void createCalender() throws Exception {
        int databaseSizeBeforeCreate = calenderRepository.findAll().size();

        // Create the Calender

        restCalenderMockMvc.perform(post("/api/calenders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calender)))
            .andExpect(status().isCreated());

        // Validate the Calender in the database
        List<Calender> calenderList = calenderRepository.findAll();
        assertThat(calenderList).hasSize(databaseSizeBeforeCreate + 1);
        Calender testCalender = calenderList.get(calenderList.size() - 1);
        assertThat(testCalender.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCalender.getDateBegining()).isEqualTo(DEFAULT_DATE_BEGINING);
        assertThat(testCalender.getDateEnding()).isEqualTo(DEFAULT_DATE_ENDING);
    }

    @Test
    @Transactional
    public void createCalenderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = calenderRepository.findAll().size();

        // Create the Calender with an existing ID
        Calender existingCalender = new Calender();
        existingCalender.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCalenderMockMvc.perform(post("/api/calenders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingCalender)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Calender> calenderList = calenderRepository.findAll();
        assertThat(calenderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = calenderRepository.findAll().size();
        // set the field null
        calender.setTitle(null);

        // Create the Calender, which fails.

        restCalenderMockMvc.perform(post("/api/calenders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calender)))
            .andExpect(status().isBadRequest());

        List<Calender> calenderList = calenderRepository.findAll();
        assertThat(calenderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateBeginingIsRequired() throws Exception {
        int databaseSizeBeforeTest = calenderRepository.findAll().size();
        // set the field null
        calender.setDateBegining(null);

        // Create the Calender, which fails.

        restCalenderMockMvc.perform(post("/api/calenders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calender)))
            .andExpect(status().isBadRequest());

        List<Calender> calenderList = calenderRepository.findAll();
        assertThat(calenderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCalenders() throws Exception {
        // Initialize the database
        calenderRepository.saveAndFlush(calender);

        // Get all the calenderList
        restCalenderMockMvc.perform(get("/api/calenders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calender.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].dateBegining").value(hasItem(DEFAULT_DATE_BEGINING.toString())))
            .andExpect(jsonPath("$.[*].dateEnding").value(hasItem(DEFAULT_DATE_ENDING.toString())));
    }

    @Test
    @Transactional
    public void getCalender() throws Exception {
        // Initialize the database
        calenderRepository.saveAndFlush(calender);

        // Get the calender
        restCalenderMockMvc.perform(get("/api/calenders/{id}", calender.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(calender.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.dateBegining").value(DEFAULT_DATE_BEGINING.toString()))
            .andExpect(jsonPath("$.dateEnding").value(DEFAULT_DATE_ENDING.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCalender() throws Exception {
        // Get the calender
        restCalenderMockMvc.perform(get("/api/calenders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCalender() throws Exception {
        // Initialize the database
        calenderRepository.saveAndFlush(calender);
        int databaseSizeBeforeUpdate = calenderRepository.findAll().size();

        // Update the calender
        Calender updatedCalender = calenderRepository.findOne(calender.getId());
        updatedCalender
                .title(UPDATED_TITLE)
                .dateBegining(UPDATED_DATE_BEGINING)
                .dateEnding(UPDATED_DATE_ENDING);

        restCalenderMockMvc.perform(put("/api/calenders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCalender)))
            .andExpect(status().isOk());

        // Validate the Calender in the database
        List<Calender> calenderList = calenderRepository.findAll();
        assertThat(calenderList).hasSize(databaseSizeBeforeUpdate);
        Calender testCalender = calenderList.get(calenderList.size() - 1);
        assertThat(testCalender.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCalender.getDateBegining()).isEqualTo(UPDATED_DATE_BEGINING);
        assertThat(testCalender.getDateEnding()).isEqualTo(UPDATED_DATE_ENDING);
    }

    @Test
    @Transactional
    public void updateNonExistingCalender() throws Exception {
        int databaseSizeBeforeUpdate = calenderRepository.findAll().size();

        // Create the Calender

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCalenderMockMvc.perform(put("/api/calenders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calender)))
            .andExpect(status().isCreated());

        // Validate the Calender in the database
        List<Calender> calenderList = calenderRepository.findAll();
        assertThat(calenderList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCalender() throws Exception {
        // Initialize the database
        calenderRepository.saveAndFlush(calender);
        int databaseSizeBeforeDelete = calenderRepository.findAll().size();

        // Get the calender
        restCalenderMockMvc.perform(delete("/api/calenders/{id}", calender.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Calender> calenderList = calenderRepository.findAll();
        assertThat(calenderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
