package fr.iut.lens.lensleau.repository;

import fr.iut.lens.lensleau.domain.Stock;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Stock entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockRepository extends JpaRepository<Stock, Long>, JpaSpecificationExecutor<Stock> {

    @Query("select stock from Stock stock where stock.jhi_user.login = ?#{principal.username}")
    List<Stock> findByJhi_userIsCurrentUser();

}
