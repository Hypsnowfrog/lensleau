package fr.iut.lens.lensleau.repository;

import fr.iut.lens.lensleau.domain.CoffeeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CoffeeType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CoffeeTypeRepository extends JpaRepository<CoffeeType, Long>, JpaSpecificationExecutor<CoffeeType> {

}
