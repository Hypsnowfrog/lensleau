package fr.iut.lens.lensleau.web.rest;
import fr.iut.lens.lensleau.domain.Stock;
import fr.iut.lens.lensleau.service.StockService;
import fr.iut.lens.lensleau.web.rest.errors.BadRequestAlertException;
import fr.iut.lens.lensleau.web.rest.util.HeaderUtil;
import fr.iut.lens.lensleau.service.dto.StockCriteria;
import fr.iut.lens.lensleau.service.StockQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Stock.
 */
@RestController
@RequestMapping("/api")
public class StockResource {

    private final Logger log = LoggerFactory.getLogger(StockResource.class);

    private static final String ENTITY_NAME = "stock";

    private final StockService stockService;

    private final StockQueryService stockQueryService;

    public StockResource(StockService stockService, StockQueryService stockQueryService) {
        this.stockService = stockService;
        this.stockQueryService = stockQueryService;
    }

    /**
     * POST  /stocks : Create a new stock.
     *
     * @param stock the stock to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stock, or with status 400 (Bad Request) if the stock has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stocks")
    public ResponseEntity<Stock> createStock(@RequestBody Stock stock) throws URISyntaxException {
        log.debug("REST request to save Stock : {}", stock);
        if (stock.getId() != null) {
            throw new BadRequestAlertException("A new stock cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Stock result = stockService.save(stock);
        return ResponseEntity.created(new URI("/api/stocks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stocks : Updates an existing stock.
     *
     * @param stock the stock to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stock,
     * or with status 400 (Bad Request) if the stock is not valid,
     * or with status 500 (Internal Server Error) if the stock couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stocks")
    public ResponseEntity<Stock> updateStock(@RequestBody Stock stock) throws URISyntaxException {
        log.debug("REST request to update Stock : {}", stock);
        if (stock.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Stock result = stockService.save(stock);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stock.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stocks : get all the stocks.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of stocks in body
     */
    @GetMapping("/stocks")
    public ResponseEntity<List<Stock>> getAllStocks(StockCriteria criteria) {
        log.debug("REST request to get Stocks by criteria: {}", criteria);
        List<Stock> entityList = stockQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * GET  /stocks/count : count all the stocks.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/stocks/count")
    public ResponseEntity<Long> countStocks(StockCriteria criteria) {
        log.debug("REST request to count Stocks by criteria: {}", criteria);
        return ResponseEntity.ok().body(stockQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /stocks/:id : get the "id" stock.
     *
     * @param id the id of the stock to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stock, or with status 404 (Not Found)
     */
    @GetMapping("/stocks/{id}")
    public ResponseEntity<Stock> getStock(@PathVariable Long id) {
        log.debug("REST request to get Stock : {}", id);
        Optional<Stock> stock = stockService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stock);
    }

    /**
     * DELETE  /stocks/:id : delete the "id" stock.
     *
     * @param id the id of the stock to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stocks/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        log.debug("REST request to delete Stock : {}", id);
        stockService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
