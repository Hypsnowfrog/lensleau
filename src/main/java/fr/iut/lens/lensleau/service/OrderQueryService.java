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

import fr.iut.lens.lensleau.domain.Order;
import fr.iut.lens.lensleau.domain.*; // for static metamodels
import fr.iut.lens.lensleau.repository.OrderRepository;
import fr.iut.lens.lensleau.service.dto.OrderCriteria;

/**
 * Service for executing complex queries for Order entities in the database.
 * The main input is a {@link OrderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Order} or a {@link Page} of {@link Order} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderQueryService extends QueryService<Order> {

    private final Logger log = LoggerFactory.getLogger(OrderQueryService.class);

    private final OrderRepository orderRepository;

    public OrderQueryService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Return a {@link List} of {@link Order} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Order> findByCriteria(OrderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Order> specification = createSpecification(criteria);
        return orderRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Order} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Order> findByCriteria(OrderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Order> specification = createSpecification(criteria);
        return orderRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Order> specification = createSpecification(criteria);
        return orderRepository.count(specification);
    }

    /**
     * Function to convert OrderCriteria to a {@link Specification}
     */
    private Specification<Order> createSpecification(OrderCriteria criteria) {
        Specification<Order> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Order_.id));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), Order_.quantity));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Order_.date));
            }
            if (criteria.getExporterId() != null) {
                specification = specification.and(buildSpecification(criteria.getExporterId(),
                    root -> root.join(Order_.exporter, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getImporterId() != null) {
                specification = specification.and(buildSpecification(criteria.getImporterId(),
                    root -> root.join(Order_.importer, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getCoffee_typeId() != null) {
                specification = specification.and(buildSpecification(criteria.getCoffee_typeId(),
                    root -> root.join(Order_.coffee_type, JoinType.LEFT).get(CoffeeType_.id)));
            }
        }
        return specification;
    }
}
