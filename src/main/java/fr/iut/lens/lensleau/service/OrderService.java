package fr.iut.lens.lensleau.service;

import fr.iut.lens.lensleau.domain.Order;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Order.
 */
public interface OrderService {

    /**
     * Save a order.
     *
     * @param order the entity to save
     * @return the persisted entity
     */
    Order save(Order order);

    /**
     * Get all the orders.
     *
     * @return the list of entities
     */
    List<Order> findAll();


    /**
     * Get the "id" order.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Order> findOne(Long id);

    /**
     * Delete the "id" order.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
