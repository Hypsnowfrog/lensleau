package fr.iut.lens.lensleau.web.rest;

import fr.iut.lens.lensleau.LensleauApp;

import fr.iut.lens.lensleau.domain.Stock;
import fr.iut.lens.lensleau.domain.User;
import fr.iut.lens.lensleau.domain.CoffeeType;
import fr.iut.lens.lensleau.repository.StockRepository;
import fr.iut.lens.lensleau.service.StockService;
import fr.iut.lens.lensleau.web.rest.errors.ExceptionTranslator;
import fr.iut.lens.lensleau.service.dto.StockCriteria;
import fr.iut.lens.lensleau.service.StockQueryService;

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
 * Test class for the StockResource REST controller.
 *
 * @see StockResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LensleauApp.class)
public class StockResourceIntTest {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockService stockService;

    @Autowired
    private StockQueryService stockQueryService;

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

    private MockMvc restStockMockMvc;

    private Stock stock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StockResource stockResource = new StockResource(stockService, stockQueryService);
        this.restStockMockMvc = MockMvcBuilders.standaloneSetup(stockResource)
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
    public static Stock createEntity(EntityManager em) {
        Stock stock = new Stock()
            .quantity(DEFAULT_QUANTITY);
        return stock;
    }

    @Before
    public void initTest() {
        stock = createEntity(em);
    }

    @Test
    @Transactional
    public void createStock() throws Exception {
        int databaseSizeBeforeCreate = stockRepository.findAll().size();

        // Create the Stock
        restStockMockMvc.perform(post("/api/stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stock)))
            .andExpect(status().isCreated());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeCreate + 1);
        Stock testStock = stockList.get(stockList.size() - 1);
        assertThat(testStock.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    public void createStockWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockRepository.findAll().size();

        // Create the Stock with an existing ID
        stock.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockMockMvc.perform(post("/api/stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stock)))
            .andExpect(status().isBadRequest());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllStocks() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList
        restStockMockMvc.perform(get("/api/stocks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stock.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }
    
    @Test
    @Transactional
    public void getStock() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get the stock
        restStockMockMvc.perform(get("/api/stocks/{id}", stock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stock.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    @Transactional
    public void getAllStocksByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where quantity equals to DEFAULT_QUANTITY
        defaultStockShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the stockList where quantity equals to UPDATED_QUANTITY
        defaultStockShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllStocksByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultStockShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the stockList where quantity equals to UPDATED_QUANTITY
        defaultStockShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllStocksByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where quantity is not null
        defaultStockShouldBeFound("quantity.specified=true");

        // Get all the stockList where quantity is null
        defaultStockShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllStocksByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where quantity greater than or equals to DEFAULT_QUANTITY
        defaultStockShouldBeFound("quantity.greaterOrEqualThan=" + DEFAULT_QUANTITY);

        // Get all the stockList where quantity greater than or equals to UPDATED_QUANTITY
        defaultStockShouldNotBeFound("quantity.greaterOrEqualThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllStocksByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where quantity less than or equals to DEFAULT_QUANTITY
        defaultStockShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the stockList where quantity less than or equals to UPDATED_QUANTITY
        defaultStockShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }


    @Test
    @Transactional
    public void getAllStocksByJhi_userIsEqualToSomething() throws Exception {
        // Initialize the database
        User jhi_user = UserResourceIntTest.createEntity(em);
        em.persist(jhi_user);
        em.flush();
        stock.setJhi_user(jhi_user);
        stockRepository.saveAndFlush(stock);
        Long jhi_userId = jhi_user.getId();

        // Get all the stockList where jhi_user equals to jhi_userId
        defaultStockShouldBeFound("jhi_userId.equals=" + jhi_userId);

        // Get all the stockList where jhi_user equals to jhi_userId + 1
        defaultStockShouldNotBeFound("jhi_userId.equals=" + (jhi_userId + 1));
    }


    @Test
    @Transactional
    public void getAllStocksByCoffee_typeIsEqualToSomething() throws Exception {
        // Initialize the database
        CoffeeType coffee_type = CoffeeTypeResourceIntTest.createEntity(em);
        em.persist(coffee_type);
        em.flush();
        stock.setCoffee_type(coffee_type);
        stockRepository.saveAndFlush(stock);
        Long coffee_typeId = coffee_type.getId();

        // Get all the stockList where coffee_type equals to coffee_typeId
        defaultStockShouldBeFound("coffee_typeId.equals=" + coffee_typeId);

        // Get all the stockList where coffee_type equals to coffee_typeId + 1
        defaultStockShouldNotBeFound("coffee_typeId.equals=" + (coffee_typeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultStockShouldBeFound(String filter) throws Exception {
        restStockMockMvc.perform(get("/api/stocks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stock.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));

        // Check, that the count call also returns 1
        restStockMockMvc.perform(get("/api/stocks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultStockShouldNotBeFound(String filter) throws Exception {
        restStockMockMvc.perform(get("/api/stocks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStockMockMvc.perform(get("/api/stocks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingStock() throws Exception {
        // Get the stock
        restStockMockMvc.perform(get("/api/stocks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStock() throws Exception {
        // Initialize the database
        stockService.save(stock);

        int databaseSizeBeforeUpdate = stockRepository.findAll().size();

        // Update the stock
        Stock updatedStock = stockRepository.findById(stock.getId()).get();
        // Disconnect from session so that the updates on updatedStock are not directly saved in db
        em.detach(updatedStock);
        updatedStock
            .quantity(UPDATED_QUANTITY);

        restStockMockMvc.perform(put("/api/stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStock)))
            .andExpect(status().isOk());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
        Stock testStock = stockList.get(stockList.size() - 1);
        assertThat(testStock.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void updateNonExistingStock() throws Exception {
        int databaseSizeBeforeUpdate = stockRepository.findAll().size();

        // Create the Stock

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockMockMvc.perform(put("/api/stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stock)))
            .andExpect(status().isBadRequest());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStock() throws Exception {
        // Initialize the database
        stockService.save(stock);

        int databaseSizeBeforeDelete = stockRepository.findAll().size();

        // Delete the stock
        restStockMockMvc.perform(delete("/api/stocks/{id}", stock.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Stock.class);
        Stock stock1 = new Stock();
        stock1.setId(1L);
        Stock stock2 = new Stock();
        stock2.setId(stock1.getId());
        assertThat(stock1).isEqualTo(stock2);
        stock2.setId(2L);
        assertThat(stock1).isNotEqualTo(stock2);
        stock1.setId(null);
        assertThat(stock1).isNotEqualTo(stock2);
    }
}
