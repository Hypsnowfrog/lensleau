package fr.iut.lens.lensleau.service.impl;

import fr.iut.lens.lensleau.service.CoffeeTypeService;
import fr.iut.lens.lensleau.domain.CoffeeType;
import fr.iut.lens.lensleau.repository.CoffeeTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing CoffeeType.
 */
@Service
@Transactional
public class CoffeeTypeServiceImpl implements CoffeeTypeService {

    private final Logger log = LoggerFactory.getLogger(CoffeeTypeServiceImpl.class);

    private final CoffeeTypeRepository coffeeTypeRepository;

    public CoffeeTypeServiceImpl(CoffeeTypeRepository coffeeTypeRepository) {
        this.coffeeTypeRepository = coffeeTypeRepository;
    }

    /**
     * Save a coffeeType.
     *
     * @param coffeeType the entity to save
     * @return the persisted entity
     */
    @Override
    public CoffeeType save(CoffeeType coffeeType) {
        log.debug("Request to save CoffeeType : {}", coffeeType);
        return coffeeTypeRepository.save(coffeeType);
    }

    /**
     * Get all the coffeeTypes.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<CoffeeType> findAll() {
        log.debug("Request to get all CoffeeTypes");
        return coffeeTypeRepository.findAll();
    }


    /**
     * Get one coffeeType by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CoffeeType> findOne(Long id) {
        log.debug("Request to get CoffeeType : {}", id);
        return coffeeTypeRepository.findById(id);
    }

    /**
     * Delete the coffeeType by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CoffeeType : {}", id);
        coffeeTypeRepository.deleteById(id);
    }
}
