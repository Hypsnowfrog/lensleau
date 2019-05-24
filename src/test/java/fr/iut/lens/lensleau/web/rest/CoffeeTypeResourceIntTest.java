package fr.iut.lens.lensleau.web.rest;

import fr.iut.lens.lensleau.LensleauApp;

import fr.iut.lens.lensleau.domain.CoffeeType;
import fr.iut.lens.lensleau.repository.CoffeeTypeRepository;
import fr.iut.lens.lensleau.service.CoffeeTypeService;
import fr.iut.lens.lensleau.web.rest.errors.ExceptionTranslator;
import fr.iut.lens.lensleau.service.dto.CoffeeTypeCriteria;
import fr.iut.lens.lensleau.service.CoffeeTypeQueryService;

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
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;


import static fr.iut.lens.lensleau.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CoffeeTypeResource REST controller.
 *
 * @see CoffeeTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LensleauApp.class)
public class CoffeeTypeResourceIntTest {

    private static final String DEFAULT_COFFEE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_COFFEE_TYPE = "BBBBBBBBBB";

    @Autowired
    private CoffeeTypeRepository coffeeTypeRepository;

    @Autowired
    private CoffeeTypeService coffeeTypeService;

    @Autowired
    private CoffeeTypeQueryService coffeeTypeQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restCoffeeTypeMockMvc;

    private CoffeeType coffeeType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CoffeeTypeResource coffeeTypeResource = new CoffeeTypeResource(coffeeTypeService, coffeeTypeQueryService);
        this.restCoffeeTypeMockMvc = MockMvcBuilders.standaloneSetup(coffeeTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CoffeeType createEntity(EntityManager em) {
        CoffeeType coffeeType = new CoffeeType()
            .coffeeType(DEFAULT_COFFEE_TYPE);
        return coffeeType;
    }

    @Before
    public void initTest() {
        coffeeType = createEntity(em);
    }

    @Test
    @Transactional
    public void createCoffeeType() throws Exception {
        int databaseSizeBeforeCreate = coffeeTypeRepository.findAll().size();

        // Create the CoffeeType
        restCoffeeTypeMockMvc.perform(post("/api/coffee-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coffeeType)))
            .andExpect(status().isCreated());

        // Validate the CoffeeType in the database
        List<CoffeeType> coffeeTypeList = coffeeTypeRepository.findAll();
        assertThat(coffeeTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CoffeeType testCoffeeType = coffeeTypeList.get(coffeeTypeList.size() - 1);
        assertThat(testCoffeeType.getCoffeeType()).isEqualTo(DEFAULT_COFFEE_TYPE);
    }

    @Test
    @Transactional
    public void createCoffeeTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = coffeeTypeRepository.findAll().size();

        // Create the CoffeeType with an existing ID
        coffeeType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoffeeTypeMockMvc.perform(post("/api/coffee-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coffeeType)))
            .andExpect(status().isBadRequest());

        // Validate the CoffeeType in the database
        List<CoffeeType> coffeeTypeList = coffeeTypeRepository.findAll();
        assertThat(coffeeTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCoffeeTypes() throws Exception {
        // Initialize the database
        coffeeTypeRepository.saveAndFlush(coffeeType);

        // Get all the coffeeTypeList
        restCoffeeTypeMockMvc.perform(get("/api/coffee-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coffeeType.getId().intValue())))
            .andExpect(jsonPath("$.[*].coffeeType").value(hasItem(DEFAULT_COFFEE_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getCoffeeType() throws Exception {
        // Initialize the database
        coffeeTypeRepository.saveAndFlush(coffeeType);

        // Get the coffeeType
        restCoffeeTypeMockMvc.perform(get("/api/coffee-types/{id}", coffeeType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(coffeeType.getId().intValue()))
            .andExpect(jsonPath("$.coffeeType").value(DEFAULT_COFFEE_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getAllCoffeeTypesByCoffeeTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        coffeeTypeRepository.saveAndFlush(coffeeType);

        // Get all the coffeeTypeList where coffeeType equals to DEFAULT_COFFEE_TYPE
        defaultCoffeeTypeShouldBeFound("coffeeType.equals=" + DEFAULT_COFFEE_TYPE);

        // Get all the coffeeTypeList where coffeeType equals to UPDATED_COFFEE_TYPE
        defaultCoffeeTypeShouldNotBeFound("coffeeType.equals=" + UPDATED_COFFEE_TYPE);
    }

    @Test
    @Transactional
    public void getAllCoffeeTypesByCoffeeTypeIsInShouldWork() throws Exception {
        // Initialize the database
        coffeeTypeRepository.saveAndFlush(coffeeType);

        // Get all the coffeeTypeList where coffeeType in DEFAULT_COFFEE_TYPE or UPDATED_COFFEE_TYPE
        defaultCoffeeTypeShouldBeFound("coffeeType.in=" + DEFAULT_COFFEE_TYPE + "," + UPDATED_COFFEE_TYPE);

        // Get all the coffeeTypeList where coffeeType equals to UPDATED_COFFEE_TYPE
        defaultCoffeeTypeShouldNotBeFound("coffeeType.in=" + UPDATED_COFFEE_TYPE);
    }

    @Test
    @Transactional
    public void getAllCoffeeTypesByCoffeeTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        coffeeTypeRepository.saveAndFlush(coffeeType);

        // Get all the coffeeTypeList where coffeeType is not null
        defaultCoffeeTypeShouldBeFound("coffeeType.specified=true");

        // Get all the coffeeTypeList where coffeeType is null
        defaultCoffeeTypeShouldNotBeFound("coffeeType.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCoffeeTypeShouldBeFound(String filter) throws Exception {
        restCoffeeTypeMockMvc.perform(get("/api/coffee-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coffeeType.getId().intValue())))
            .andExpect(jsonPath("$.[*].coffeeType").value(hasItem(DEFAULT_COFFEE_TYPE)));

        // Check, that the count call also returns 1
        restCoffeeTypeMockMvc.perform(get("/api/coffee-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCoffeeTypeShouldNotBeFound(String filter) throws Exception {
        restCoffeeTypeMockMvc.perform(get("/api/coffee-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCoffeeTypeMockMvc.perform(get("/api/coffee-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCoffeeType() throws Exception {
        // Get the coffeeType
        restCoffeeTypeMockMvc.perform(get("/api/coffee-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCoffeeType() throws Exception {
        // Initialize the database
        coffeeTypeService.save(coffeeType);

        int databaseSizeBeforeUpdate = coffeeTypeRepository.findAll().size();

        // Update the coffeeType
        CoffeeType updatedCoffeeType = coffeeTypeRepository.findById(coffeeType.getId()).get();
        // Disconnect from session so that the updates on updatedCoffeeType are not directly saved in db
        em.detach(updatedCoffeeType);
        updatedCoffeeType
            .coffeeType(UPDATED_COFFEE_TYPE);

        restCoffeeTypeMockMvc.perform(put("/api/coffee-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCoffeeType)))
            .andExpect(status().isOk());

        // Validate the CoffeeType in the database
        List<CoffeeType> coffeeTypeList = coffeeTypeRepository.findAll();
        assertThat(coffeeTypeList).hasSize(databaseSizeBeforeUpdate);
        CoffeeType testCoffeeType = coffeeTypeList.get(coffeeTypeList.size() - 1);
        assertThat(testCoffeeType.getCoffeeType()).isEqualTo(UPDATED_COFFEE_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingCoffeeType() throws Exception {
        int databaseSizeBeforeUpdate = coffeeTypeRepository.findAll().size();

        // Create the CoffeeType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoffeeTypeMockMvc.perform(put("/api/coffee-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coffeeType)))
            .andExpect(status().isBadRequest());

        // Validate the CoffeeType in the database
        List<CoffeeType> coffeeTypeList = coffeeTypeRepository.findAll();
        assertThat(coffeeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCoffeeType() throws Exception {
        // Initialize the database
        coffeeTypeService.save(coffeeType);

        int databaseSizeBeforeDelete = coffeeTypeRepository.findAll().size();

        // Delete the coffeeType
        restCoffeeTypeMockMvc.perform(delete("/api/coffee-types/{id}", coffeeType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CoffeeType> coffeeTypeList = coffeeTypeRepository.findAll();
        assertThat(coffeeTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CoffeeType.class);
        CoffeeType coffeeType1 = new CoffeeType();
        coffeeType1.setId(1L);
        CoffeeType coffeeType2 = new CoffeeType();
        coffeeType2.setId(coffeeType1.getId());
        assertThat(coffeeType1).isEqualTo(coffeeType2);
        coffeeType2.setId(2L);
        assertThat(coffeeType1).isNotEqualTo(coffeeType2);
        coffeeType1.setId(null);
        assertThat(coffeeType1).isNotEqualTo(coffeeType2);
    }
}
