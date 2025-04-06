package com.epam.rd.autocode.assessment.appliances.repository;

import com.epam.rd.autocode.assessment.appliances.model.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface OrdersRepository extends JpaRepository <Orders, Long> {
    Page<Orders> findAll(Pageable pageable);
    Page<Orders> findAllByClient_Id(Long clientId, Pageable pageable);
    Page<Orders> findByClient_NameContainingIgnoreCase(String search, Pageable pageable);
    Page<Orders> findAllByClient_Id(Long clientId, String search, Pageable pageable);

    @Query("SELECT o FROM Orders o WHERE o.client.id = :clientId")
    Page<Orders> findByClientId(@Param("clientId") Long clientId, Pageable pageable);

    // Для поиска по заказам клиента
    @Query("SELECT o FROM Orders o WHERE o.client.id = :clientId AND " +
            "(LOWER(o.client.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(o.employee.name) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Orders> searchForClient(@Param("clientId") Long clientId,
                                 @Param("search") String search,
                                 Pageable pageable);

    /*@Query("SELECT o FROM OrderRow o ORDER BY (o.amount * o.number) ASC")
    Page<Orders> findAllWithComputedTotalAmount(Pageable pageable);*/
}
