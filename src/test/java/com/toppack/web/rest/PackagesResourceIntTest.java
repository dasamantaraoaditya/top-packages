package com.toppack.web.rest;

import com.toppack.ToppackApp;

import com.toppack.domain.Packages;
import com.toppack.repository.PackagesRepository;
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
 * Test class for the PackagesResource REST controller.
 *
 * @see PackagesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ToppackApp.class)
public class PackagesResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private PackagesRepository packagesRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPackagesMockMvc;

    private Packages packages;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PackagesResource packagesResource = new PackagesResource(packagesRepository);
        this.restPackagesMockMvc = MockMvcBuilders.standaloneSetup(packagesResource)
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
    public static Packages createEntity(EntityManager em) {
        Packages packages = new Packages()
            .name(DEFAULT_NAME);
        return packages;
    }

    @Before
    public void initTest() {
        packages = createEntity(em);
    }

    @Test
    @Transactional
    public void createPackages() throws Exception {
        int databaseSizeBeforeCreate = packagesRepository.findAll().size();

        // Create the Packages
        restPackagesMockMvc.perform(post("/api/packages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packages)))
            .andExpect(status().isCreated());

        // Validate the Packages in the database
        List<Packages> packagesList = packagesRepository.findAll();
        assertThat(packagesList).hasSize(databaseSizeBeforeCreate + 1);
        Packages testPackages = packagesList.get(packagesList.size() - 1);
        assertThat(testPackages.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createPackagesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = packagesRepository.findAll().size();

        // Create the Packages with an existing ID
        packages.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPackagesMockMvc.perform(post("/api/packages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packages)))
            .andExpect(status().isBadRequest());

        // Validate the Packages in the database
        List<Packages> packagesList = packagesRepository.findAll();
        assertThat(packagesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPackages() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        // Get all the packagesList
        restPackagesMockMvc.perform(get("/api/packages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(packages.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getPackages() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        // Get the packages
        restPackagesMockMvc.perform(get("/api/packages/{id}", packages.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(packages.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPackages() throws Exception {
        // Get the packages
        restPackagesMockMvc.perform(get("/api/packages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePackages() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);
        int databaseSizeBeforeUpdate = packagesRepository.findAll().size();

        // Update the packages
        Packages updatedPackages = packagesRepository.findOne(packages.getId());
        updatedPackages
            .name(UPDATED_NAME);

        restPackagesMockMvc.perform(put("/api/packages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPackages)))
            .andExpect(status().isOk());

        // Validate the Packages in the database
        List<Packages> packagesList = packagesRepository.findAll();
        assertThat(packagesList).hasSize(databaseSizeBeforeUpdate);
        Packages testPackages = packagesList.get(packagesList.size() - 1);
        assertThat(testPackages.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingPackages() throws Exception {
        int databaseSizeBeforeUpdate = packagesRepository.findAll().size();

        // Create the Packages

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPackagesMockMvc.perform(put("/api/packages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packages)))
            .andExpect(status().isCreated());

        // Validate the Packages in the database
        List<Packages> packagesList = packagesRepository.findAll();
        assertThat(packagesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePackages() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);
        int databaseSizeBeforeDelete = packagesRepository.findAll().size();

        // Get the packages
        restPackagesMockMvc.perform(delete("/api/packages/{id}", packages.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Packages> packagesList = packagesRepository.findAll();
        assertThat(packagesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Packages.class);
        Packages packages1 = new Packages();
        packages1.setId(1L);
        Packages packages2 = new Packages();
        packages2.setId(packages1.getId());
        assertThat(packages1).isEqualTo(packages2);
        packages2.setId(2L);
        assertThat(packages1).isNotEqualTo(packages2);
        packages1.setId(null);
        assertThat(packages1).isNotEqualTo(packages2);
    }
}
