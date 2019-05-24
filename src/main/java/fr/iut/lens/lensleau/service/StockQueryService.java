package fr.iut.lens.lensleau.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import fr.iut.lens.lensleau.domain.Stock;
import fr.iut.lens.lensleau.domain.*; // for static metamodels
import fr.iut.lens.lensleau.repository.StockRepository;
import fr.iut.lens.lensleau.service.dto.StockCriteria;

/**
 * Service for executing complex queries for Stock entities in the database.
 * The main input is a {@link StockCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Stock} or a {@link Page} of {@link Stock} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StockQueryService extends QueryService<Stock> {

    private final Logger log = LoggerFactory.getLogger(StockQueryService.class);

    private final StockRepository stockRepository;

    public StockQueryService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    /**
     * Return a {@link List} of {@link Stock} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Stock> findByCriteria(StockCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Stock> specification = createSpecification(criteria);
        return stockRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Stock} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Stock> findByCriteria(StockCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Stock> specification = createSpecification(criteria);
        return stockRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StockCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Stock> specification = createSpecification(criteria);
        return stockRepository.count(specification);
    }

    /**
     * Function to convert StockCriteria to a {@link Specification}
     */
    private Specification<Stock> createSpecification(StockCriteria criteria) {
        Specification<Stock> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Stock_.id));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), Stock_.quantity));
            }
            if (criteria.getJhi_userId() != null) {
                specification = specification.and(buildSpecification(criteria.getJhi_userId(),
                    root -> root.join(Stock_.jhi_user, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getCoffee_typeId() != null) {
                specification = specification.and(buildSpecification(criteria.getCoffee_typeId(),
                    root -> root.join(Stock_.coffee_type, JoinType.LEFT).get(CoffeeType_.id)));
            }
        }
        return specification;
    }
}
