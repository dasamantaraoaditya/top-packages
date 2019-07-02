package com.toppack.web.rest;

import com.toppack.ToppackApp;

import com.toppack.domain.Repository;
import com.toppack.repository.RepositoryRepository;
import com.toppack.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.toppack.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RepositoryResource REST controller.
 *
 * @see RepositoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ToppackApp.class)
public class RepositoryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_STARS = 1;
    private static final Integer UPDATED_STARS = 2;

    private static final Integer DEFAULT_FORKS = 1;
    private static final Integer UPDATED_FORKS = 2;

    @Autowired
    private RepositoryRepository repositoryRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRepositoryMockMvc;

    private Repository repository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RepositoryResource repositoryResource = new RepositoryResource(repositoryRepository);
        this.restRepositoryMockMvc = MockMvcBuilders.standaloneSetup(repositoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Repository createEntity(EntityManager em) {
        Repository repository = new Repository()
            .name(DEFAULT_NAME)
            .stars(DEFAULT_STARS)
            .forks(DEFAULT_FORKS);
        return repository;
    }

    @Before
    public void initTest() {
        repository = createEntity(em);
    }

    @Test
    @Transactional
    public void createRepository() throws Exception {
        int databaseSizeBeforeCreate = repositoryRepository.findAll().size();

        // Create the Repository
        restRepositoryMockMvc.perform(post("/api/repositories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(repository)))
            .andExpect(status().isCreated());

        // Validate the Repository in the database
        List<Repository> repositoryList = repositoryRepository.findAll();
        assertThat(repositoryList).hasSize(databaseSizeBeforeCreate + 1);
        Repository testRepository = repositoryList.get(repositoryList.size() - 1);
        assertThat(testRepository.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRepository.getStars()).isEqualTo(DEFAULT_STARS);
        assertThat(testRepository.getForks()).isEqualTo(DEFAULT_FORKS);
    }

    @Test
    @Transactional
    public void createRepositoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = repositoryRepository.findAll().size();

        // Create the Repository with an existing ID
        repository.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRepositoryMockMvc.perform(post("/api/repositories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(repository)))
            .andExpect(status().isBadRequest());

        // Validate the Repository in the database
        List<Repository> repositoryList = repositoryRepository.findAll();
        assertThat(repositoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRepositories() throws Exception {
        // Initialize the database
        repositoryRepository.saveAndFlush(repository);

        // Get all the repositoryList
        restRepositoryMockMvc.perform(get("/api/repositories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(repository.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].stars").value(hasItem(DEFAULT_STARS)))
            .andExpect(jsonPath("$.[*].forks").value(hasItem(DEFAULT_FORKS)));
    }

    @Test
    @Transactional
    public void getRepository() throws Exception {
        // Initialize the database
        repositoryRepository.saveAndFlush(repository);

        // Get the repository
        restRepositoryMockMvc.perform(get("/api/repositories/{id}", repository.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(repository.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.stars").value(DEFAULT_STARS))
            .andExpect(jsonPath("$.forks").value(DEFAULT_FORKS));
    }

    @Test
    @Transactional
    public void getNonExistingRepository() throws Exception {
        // Get the repository
        restRepositoryMockMvc.perform(get("/api/repositories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRepository() throws Exception {
        // Initialize the database
        repositoryRepository.saveAndFlush(repository);
        int databaseSizeBeforeUpdate = repositoryRepository.findAll().size();

        // Update the repository
        Repository updatedRepository = repositoryRepository.findOne(repository.getId());
        updatedRepository
            .name(UPDATED_NAME)
            .stars(UPDATED_STARS)
            .forks(UPDATED_FORKS);

        restRepositoryMockMvc.perform(put("/api/repositories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRepository)))
            .andExpect(status().isOk());

        // Validate the Repository in the database
        List<Repository> repositoryList = repositoryRepository.findAll();
        assertThat(repositoryList).hasSize(databaseSizeBeforeUpdate);
        Repository testRepository = repositoryList.get(repositoryList.size() - 1);
        assertThat(testRepository.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRepository.getStars()).isEqualTo(UPDATED_STARS);
        assertThat(testRepository.getForks()).isEqualTo(UPDATED_FORKS);
    }

    @Test
    @Transactional
    public void updateNonExistingRepository() throws Exception {
        int databaseSizeBeforeUpdate = repositoryRepository.findAll().size();

        // Create the Repository

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRepositoryMockMvc.perform(put("/api/repositories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(repository)))
            .andExpect(status().isCreated());

        // Validate the Repository in the database
        List<Repository> repositoryList = repositoryRepository.findAll();
        assertThat(repositoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRepository() throws Exception {
        // Initialize the database
        repositoryRepository.saveAndFlush(repository);
        int databaseSizeBeforeDelete = repositoryRepository.findAll().size();

        // Get the repository
        restRepositoryMockMvc.perform(delete("/api/repositories/{id}", repository.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Repository> repositoryList = repositoryRepository.findAll();
        assertThat(repositoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Repository.class);
        Repository repository1 = new Repository();
        repository1.setId(1L);
        Repository repository2 = new Repository();
        repository2.setId(repository1.getId());
        assertThat(repository1).isEqualTo(repository2);
        repository2.setId(2L);
        assertThat(repository1).isNotEqualTo(repository2);
        repository1.setId(null);
        assertThat(repository1).isNotEqualTo(repository2);
    }
}
