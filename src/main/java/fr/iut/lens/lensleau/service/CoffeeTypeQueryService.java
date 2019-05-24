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

import fr.iut.lens.lensleau.domain.CoffeeType;
import fr.iut.lens.lensleau.domain.*; // for static metamodels
import fr.iut.lens.lensleau.repository.CoffeeTypeRepository;
import fr.iut.lens.lensleau.service.dto.CoffeeTypeCriteria;

/**
 * Service for executing complex queries for CoffeeType entities in the database.
 * The main input is a {@link CoffeeTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CoffeeType} or a {@link Page} of {@link CoffeeType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CoffeeTypeQueryService extends QueryService<CoffeeType> {

    private final Logger log = LoggerFactory.getLogger(CoffeeTypeQueryService.class);

    private final CoffeeTypeRepository coffeeTypeRepository;

    public CoffeeTypeQueryService(CoffeeTypeRepository coffeeTypeRepository) {
        this.coffeeTypeRepository = coffeeTypeRepository;
    }

    /**
     * Return a {@link List} of {@link CoffeeType} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CoffeeType> findByCriteria(CoffeeTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CoffeeType> specification = createSpecification(criteria);
        return coffeeTypeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CoffeeType} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CoffeeType> findByCriteria(CoffeeTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CoffeeType> specification = createSpecification(criteria);
        return coffeeTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CoffeeTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CoffeeType> specification = createSpecification(criteria);
        return coffeeTypeRepository.count(specification);
    }

    /**
     * Function to convert CoffeeTypeCriteria to a {@link Specification}
     */
    private Specification<CoffeeType> createSpecification(CoffeeTypeCriteria criteria) {
        Specification<CoffeeType> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), CoffeeType_.id));
            }
            if (criteria.getCoffeeType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCoffeeType(), CoffeeType_.coffeeType));
            }
        }
        return specification;
    }
}
