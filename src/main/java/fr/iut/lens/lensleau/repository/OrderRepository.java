package fr.iut.lens.lensleau.repository;

import fr.iut.lens.lensleau.domain.Order;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Order entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    @Query("select jhi_order from Order jhi_order where jhi_order.exporter.login = ?#{principal.username}")
    List<Order> findByExporterIsCurrentUser();

    @Query("select jhi_order from Order jhi_order where jhi_order.importer.login = ?#{principal.username}")
    List<Order> findByImporterIsCurrentUser();

}
