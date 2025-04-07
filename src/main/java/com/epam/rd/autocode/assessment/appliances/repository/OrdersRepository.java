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


}
