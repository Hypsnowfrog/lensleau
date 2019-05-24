package fr.iut.lens.lensleau.web.rest;

import fr.iut.lens.lensleau.LensleauApp;

import fr.iut.lens.lensleau.domain.Country;
import fr.iut.lens.lensleau.repository.CountryRepository;
import fr.iut.lens.lensleau.service.CountryService;
import fr.iut.lens.lensleau.web.rest.errors.ExceptionTranslator;
import fr.iut.lens.lensleau.service.dto.CountryCriteria;
import fr.iut.lens.lensleau.service.CountryQueryService;

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
 * Test class for the CountryResource REST controller.
 *
 * @see CountryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LensleauApp.class)
public class CountryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CAPITAL = "AAAAAAAAAA";
    private static final String UPDATED_CAPITAL = "BBBBBBBBBB";

    private static final Integer DEFAULT_POPULATION = 1;
    private static final Integer UPDATED_POPULATION = 2;

    private static final Integer DEFAULT_SURFACE = 1;
    private static final Integer UPDATED_SURFACE = 2;

    private static final Integer DEFAULT_COFFEE_PRODUCTION = 1;
    private static final Integer UPDATED_COFFEE_PRODUCTION = 2;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CountryService countryService;

    @Autowired
    private CountryQueryService countryQueryService;

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

    private MockMvc restCountryMockMvc;

    private Country country;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CountryResource countryResource = new CountryResource(countryService, countryQueryService);
        this.restCountryMockMvc = MockMvcBuilders.standaloneSetup(countryResource)
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
    public static Country createEntity(EntityManager em) {
        Country country = new Country()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .capital(DEFAULT_CAPITAL)
            .population(DEFAULT_POPULATION)
            .surface(DEFAULT_SURFACE)
            .coffeeProduction(DEFAULT_COFFEE_PRODUCTION);
        return country;
    }

    @Before
    public void initTest() {
        country = createEntity(em);
    }

    @Test
    @Transactional
    public void createCountry() throws Exception {
        int databaseSizeBeforeCreate = countryRepository.findAll().size();

        // Create the Country
        restCountryMockMvc.perform(post("/api/countries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(country)))
            .andExpect(status().isCreated());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeCreate + 1);
        Country testCountry = countryList.get(countryList.size() - 1);
        assertThat(testCountry.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCountry.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCountry.getCapital()).isEqualTo(DEFAULT_CAPITAL);
        assertThat(testCountry.getPopulation()).isEqualTo(DEFAULT_POPULATION);
        assertThat(testCountry.getSurface()).isEqualTo(DEFAULT_SURFACE);
        assertThat(testCountry.getCoffeeProduction()).isEqualTo(DEFAULT_COFFEE_PRODUCTION);
    }

    @Test
    @Transactional
    public void createCountryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = countryRepository.findAll().size();

        // Create the Country with an existing ID
        country.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCountryMockMvc.perform(post("/api/countries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(country)))
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCountries() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList
        restCountryMockMvc.perform(get("/api/countries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(country.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].capital").value(hasItem(DEFAULT_CAPITAL.toString())))
            .andExpect(jsonPath("$.[*].population").value(hasItem(DEFAULT_POPULATION)))
            .andExpect(jsonPath("$.[*].surface").value(hasItem(DEFAULT_SURFACE)))
            .andExpect(jsonPath("$.[*].coffeeProduction").value(hasItem(DEFAULT_COFFEE_PRODUCTION)));
    }
    
    @Test
    @Transactional
    public void getCountry() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get the country
        restCountryMockMvc.perform(get("/api/countries/{id}", country.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(country.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.capital").value(DEFAULT_CAPITAL.toString()))
            .andExpect(jsonPath("$.population").value(DEFAULT_POPULATION))
            .andExpect(jsonPath("$.surface").value(DEFAULT_SURFACE))
            .andExpect(jsonPath("$.coffeeProduction").value(DEFAULT_COFFEE_PRODUCTION));
    }

    @Test
    @Transactional
    public void getAllCountriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where name equals to DEFAULT_NAME
        defaultCountryShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the countryList where name equals to UPDATED_NAME
        defaultCountryShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCountriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCountryShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the countryList where name equals to UPDATED_NAME
        defaultCountryShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCountriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where name is not null
        defaultCountryShouldBeFound("name.specified=true");

        // Get all the countryList where name is null
        defaultCountryShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllCountriesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where description equals to DEFAULT_DESCRIPTION
        defaultCountryShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the countryList where description equals to UPDATED_DESCRIPTION
        defaultCountryShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCountriesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCountryShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the countryList where description equals to UPDATED_DESCRIPTION
        defaultCountryShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCountriesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where description is not null
        defaultCountryShouldBeFound("description.specified=true");

        // Get all the countryList where description is null
        defaultCountryShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllCountriesByCapitalIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where capital equals to DEFAULT_CAPITAL
        defaultCountryShouldBeFound("capital.equals=" + DEFAULT_CAPITAL);

        // Get all the countryList where capital equals to UPDATED_CAPITAL
        defaultCountryShouldNotBeFound("capital.equals=" + UPDATED_CAPITAL);
    }

    @Test
    @Transactional
    public void getAllCountriesByCapitalIsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where capital in DEFAULT_CAPITAL or UPDATED_CAPITAL
        defaultCountryShouldBeFound("capital.in=" + DEFAULT_CAPITAL + "," + UPDATED_CAPITAL);

        // Get all the countryList where capital equals to UPDATED_CAPITAL
        defaultCountryShouldNotBeFound("capital.in=" + UPDATED_CAPITAL);
    }

    @Test
    @Transactional
    public void getAllCountriesByCapitalIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where capital is not null
        defaultCountryShouldBeFound("capital.specified=true");

        // Get all the countryList where capital is null
        defaultCountryShouldNotBeFound("capital.specified=false");
    }

    @Test
    @Transactional
    public void getAllCountriesByPopulationIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where population equals to DEFAULT_POPULATION
        defaultCountryShouldBeFound("population.equals=" + DEFAULT_POPULATION);

        // Get all the countryList where population equals to UPDATED_POPULATION
        defaultCountryShouldNotBeFound("population.equals=" + UPDATED_POPULATION);
    }

    @Test
    @Transactional
    public void getAllCountriesByPopulationIsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where population in DEFAULT_POPULATION or UPDATED_POPULATION
        defaultCountryShouldBeFound("population.in=" + DEFAULT_POPULATION + "," + UPDATED_POPULATION);

        // Get all the countryList where population equals to UPDATED_POPULATION
        defaultCountryShouldNotBeFound("population.in=" + UPDATED_POPULATION);
    }

    @Test
    @Transactional
    public void getAllCountriesByPopulationIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where population is not null
        defaultCountryShouldBeFound("population.specified=true");

        // Get all the countryList where population is null
        defaultCountryShouldNotBeFound("population.specified=false");
    }

    @Test
    @Transactional
    public void getAllCountriesByPopulationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where population greater than or equals to DEFAULT_POPULATION
        defaultCountryShouldBeFound("population.greaterOrEqualThan=" + DEFAULT_POPULATION);

        // Get all the countryList where population greater than or equals to UPDATED_POPULATION
        defaultCountryShouldNotBeFound("population.greaterOrEqualThan=" + UPDATED_POPULATION);
    }

    @Test
    @Transactional
    public void getAllCountriesByPopulationIsLessThanSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where population less than or equals to DEFAULT_POPULATION
        defaultCountryShouldNotBeFound("population.lessThan=" + DEFAULT_POPULATION);

        // Get all the countryList where population less than or equals to UPDATED_POPULATION
        defaultCountryShouldBeFound("population.lessThan=" + UPDATED_POPULATION);
    }


    @Test
    @Transactional
    public void getAllCountriesBySurfaceIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where surface equals to DEFAULT_SURFACE
        defaultCountryShouldBeFound("surface.equals=" + DEFAULT_SURFACE);

        // Get all the countryList where surface equals to UPDATED_SURFACE
        defaultCountryShouldNotBeFound("surface.equals=" + UPDATED_SURFACE);
    }

    @Test
    @Transactional
    public void getAllCountriesBySurfaceIsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where surface in DEFAULT_SURFACE or UPDATED_SURFACE
        defaultCountryShouldBeFound("surface.in=" + DEFAULT_SURFACE + "," + UPDATED_SURFACE);

        // Get all the countryList where surface equals to UPDATED_SURFACE
        defaultCountryShouldNotBeFound("surface.in=" + UPDATED_SURFACE);
    }

    @Test
    @Transactional
    public void getAllCountriesBySurfaceIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where surface is not null
        defaultCountryShouldBeFound("surface.specified=true");

        // Get all the countryList where surface is null
        defaultCountryShouldNotBeFound("surface.specified=false");
    }

    @Test
    @Transactional
    public void getAllCountriesBySurfaceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where surface greater than or equals to DEFAULT_SURFACE
        defaultCountryShouldBeFound("surface.greaterOrEqualThan=" + DEFAULT_SURFACE);

        // Get all the countryList where surface greater than or equals to UPDATED_SURFACE
        defaultCountryShouldNotBeFound("surface.greaterOrEqualThan=" + UPDATED_SURFACE);
    }

    @Test
    @Transactional
    public void getAllCountriesBySurfaceIsLessThanSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where surface less than or equals to DEFAULT_SURFACE
        defaultCountryShouldNotBeFound("surface.lessThan=" + DEFAULT_SURFACE);

        // Get all the countryList where surface less than or equals to UPDATED_SURFACE
        defaultCountryShouldBeFound("surface.lessThan=" + UPDATED_SURFACE);
    }


    @Test
    @Transactional
    public void getAllCountriesByCoffeeProductionIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where coffeeProduction equals to DEFAULT_COFFEE_PRODUCTION
        defaultCountryShouldBeFound("coffeeProduction.equals=" + DEFAULT_COFFEE_PRODUCTION);

        // Get all the countryList where coffeeProduction equals to UPDATED_COFFEE_PRODUCTION
        defaultCountryShouldNotBeFound("coffeeProduction.equals=" + UPDATED_COFFEE_PRODUCTION);
    }

    @Test
    @Transactional
    public void getAllCountriesByCoffeeProductionIsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where coffeeProduction in DEFAULT_COFFEE_PRODUCTION or UPDATED_COFFEE_PRODUCTION
        defaultCountryShouldBeFound("coffeeProduction.in=" + DEFAULT_COFFEE_PRODUCTION + "," + UPDATED_COFFEE_PRODUCTION);

        // Get all the countryList where coffeeProduction equals to UPDATED_COFFEE_PRODUCTION
        defaultCountryShouldNotBeFound("coffeeProduction.in=" + UPDATED_COFFEE_PRODUCTION);
    }

    @Test
    @Transactional
    public void getAllCountriesByCoffeeProductionIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where coffeeProduction is not null
        defaultCountryShouldBeFound("coffeeProduction.specified=true");

        // Get all the countryList where coffeeProduction is null
        defaultCountryShouldNotBeFound("coffeeProduction.specified=false");
    }

    @Test
    @Transactional
    public void getAllCountriesByCoffeeProductionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where coffeeProduction greater than or equals to DEFAULT_COFFEE_PRODUCTION
        defaultCountryShouldBeFound("coffeeProduction.greaterOrEqualThan=" + DEFAULT_COFFEE_PRODUCTION);

        // Get all the countryList where coffeeProduction greater than or equals to UPDATED_COFFEE_PRODUCTION
        defaultCountryShouldNotBeFound("coffeeProduction.greaterOrEqualThan=" + UPDATED_COFFEE_PRODUCTION);
    }

    @Test
    @Transactional
    public void getAllCountriesByCoffeeProductionIsLessThanSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where coffeeProduction less than or equals to DEFAULT_COFFEE_PRODUCTION
        defaultCountryShouldNotBeFound("coffeeProduction.lessThan=" + DEFAULT_COFFEE_PRODUCTION);

        // Get all the countryList where coffeeProduction less than or equals to UPDATED_COFFEE_PRODUCTION
        defaultCountryShouldBeFound("coffeeProduction.lessThan=" + UPDATED_COFFEE_PRODUCTION);
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCountryShouldBeFound(String filter) throws Exception {
        restCountryMockMvc.perform(get("/api/countries?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(country.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].capital").value(hasItem(DEFAULT_CAPITAL)))
            .andExpect(jsonPath("$.[*].population").value(hasItem(DEFAULT_POPULATION)))
            .andExpect(jsonPath("$.[*].surface").value(hasItem(DEFAULT_SURFACE)))
            .andExpect(jsonPath("$.[*].coffeeProduction").value(hasItem(DEFAULT_COFFEE_PRODUCTION)));

        // Check, that the count call also returns 1
        restCountryMockMvc.perform(get("/api/countries/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCountryShouldNotBeFound(String filter) throws Exception {
        restCountryMockMvc.perform(get("/api/countries?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCountryMockMvc.perform(get("/api/countries/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCountry() throws Exception {
        // Get the country
        restCountryMockMvc.perform(get("/api/countries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCountry() throws Exception {
        // Initialize the database
        countryService.save(country);

        int databaseSizeBeforeUpdate = countryRepository.findAll().size();

        // Update the country
        Country updatedCountry = countryRepository.findById(country.getId()).get();
        // Disconnect from session so that the updates on updatedCountry are not directly saved in db
        em.detach(updatedCountry);
        updatedCountry
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .capital(UPDATED_CAPITAL)
            .population(UPDATED_POPULATION)
            .surface(UPDATED_SURFACE)
            .coffeeProduction(UPDATED_COFFEE_PRODUCTION);

        restCountryMockMvc.perform(put("/api/countries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCountry)))
            .andExpect(status().isOk());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
        Country testCountry = countryList.get(countryList.size() - 1);
        assertThat(testCountry.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCountry.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCountry.getCapital()).isEqualTo(UPDATED_CAPITAL);
        assertThat(testCountry.getPopulation()).isEqualTo(UPDATED_POPULATION);
        assertThat(testCountry.getSurface()).isEqualTo(UPDATED_SURFACE);
        assertThat(testCountry.getCoffeeProduction()).isEqualTo(UPDATED_COFFEE_PRODUCTION);
    }

    @Test
    @Transactional
    public void updateNonExistingCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().size();

        // Create the Country

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountryMockMvc.perform(put("/api/countries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(country)))
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCountry() throws Exception {
        // Initialize the database
        countryService.save(country);

        int databaseSizeBeforeDelete = countryRepository.findAll().size();

        // Delete the country
        restCountryMockMvc.perform(delete("/api/countries/{id}", country.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Country.class);
        Country country1 = new Country();
        country1.setId(1L);
        Country country2 = new Country();
        country2.setId(country1.getId());
        assertThat(country1).isEqualTo(country2);
        country2.setId(2L);
        assertThat(country1).isNotEqualTo(country2);
        country1.setId(null);
        assertThat(country1).isNotEqualTo(country2);
    }
}
