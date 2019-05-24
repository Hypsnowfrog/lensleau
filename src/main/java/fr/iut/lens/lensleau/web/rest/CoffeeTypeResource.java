package fr.iut.lens.lensleau.web.rest;
import fr.iut.lens.lensleau.domain.CoffeeType;
import fr.iut.lens.lensleau.service.CoffeeTypeService;
import fr.iut.lens.lensleau.web.rest.errors.BadRequestAlertException;
import fr.iut.lens.lensleau.web.rest.util.HeaderUtil;
import fr.iut.lens.lensleau.service.dto.CoffeeTypeCriteria;
import fr.iut.lens.lensleau.service.CoffeeTypeQueryService;
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
 * REST controller for managing CoffeeType.
 */
@RestController
@RequestMapping("/api")
public class CoffeeTypeResource {

    private final Logger log = LoggerFactory.getLogger(CoffeeTypeResource.class);

    private static final String ENTITY_NAME = "coffeeType";

    private final CoffeeTypeService coffeeTypeService;

    private final CoffeeTypeQueryService coffeeTypeQueryService;

    public CoffeeTypeResource(CoffeeTypeService coffeeTypeService, CoffeeTypeQueryService coffeeTypeQueryService) {
        this.coffeeTypeService = coffeeTypeService;
        this.coffeeTypeQueryService = coffeeTypeQueryService;
    }

    /**
     * POST  /coffee-types : Create a new coffeeType.
     *
     * @param coffeeType the coffeeType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new coffeeType, or with status 400 (Bad Request) if the coffeeType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/coffee-types")
    public ResponseEntity<CoffeeType> createCoffeeType(@RequestBody CoffeeType coffeeType) throws URISyntaxException {
        log.debug("REST request to save CoffeeType : {}", coffeeType);
        if (coffeeType.getId() != null) {
            throw new BadRequestAlertException("A new coffeeType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CoffeeType result = coffeeTypeService.save(coffeeType);
        return ResponseEntity.created(new URI("/api/coffee-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /coffee-types : Updates an existing coffeeType.
     *
     * @param coffeeType the coffeeType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated coffeeType,
     * or with status 400 (Bad Request) if the coffeeType is not valid,
     * or with status 500 (Internal Server Error) if the coffeeType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/coffee-types")
    public ResponseEntity<CoffeeType> updateCoffeeType(@RequestBody CoffeeType coffeeType) throws URISyntaxException {
        log.debug("REST request to update CoffeeType : {}", coffeeType);
        if (coffeeType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CoffeeType result = coffeeTypeService.save(coffeeType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, coffeeType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /coffee-types : get all the coffeeTypes.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of coffeeTypes in body
     */
    @GetMapping("/coffee-types")
    public ResponseEntity<List<CoffeeType>> getAllCoffeeTypes(CoffeeTypeCriteria criteria) {
        log.debug("REST request to get CoffeeTypes by criteria: {}", criteria);
        List<CoffeeType> entityList = coffeeTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * GET  /coffee-types/count : count all the coffeeTypes.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/coffee-types/count")
    public ResponseEntity<Long> countCoffeeTypes(CoffeeTypeCriteria criteria) {
        log.debug("REST request to count CoffeeTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(coffeeTypeQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /coffee-types/:id : get the "id" coffeeType.
     *
     * @param id the id of the coffeeType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the coffeeType, or with status 404 (Not Found)
     */
    @GetMapping("/coffee-types/{id}")
    public ResponseEntity<CoffeeType> getCoffeeType(@PathVariable Long id) {
        log.debug("REST request to get CoffeeType : {}", id);
        Optional<CoffeeType> coffeeType = coffeeTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(coffeeType);
    }

    /**
     * DELETE  /coffee-types/:id : delete the "id" coffeeType.
     *
     * @param id the id of the coffeeType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/coffee-types/{id}")
    public ResponseEntity<Void> deleteCoffeeType(@PathVariable Long id) {
        log.debug("REST request to delete CoffeeType : {}", id);
        coffeeTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
