package com.example.Spring_E_Commerce.repository;

import com.example.Spring_E_Commerce.model.OrderDetail;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrderID(String orderID);
}


