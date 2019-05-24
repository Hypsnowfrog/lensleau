package fr.iut.lens.lensleau.service;

import fr.iut.lens.lensleau.domain.CoffeeType;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing CoffeeType.
 */
public interface CoffeeTypeService {

    /**
     * Save a coffeeType.
     *
     * @param coffeeType the entity to save
     * @return the persisted entity
     */
    CoffeeType save(CoffeeType coffeeType);

    /**
     * Get all the coffeeTypes.
     *
     * @return the list of entities
     */
    List<CoffeeType> findAll();


    /**
     * Get the "id" coffeeType.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CoffeeType> findOne(Long id);

    /**
     * Delete the "id" coffeeType.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
