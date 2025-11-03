package com.example.Spring_E_Commerce.repository;

import com.example.Spring_E_Commerce.model.Details_Model;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface Detailrepo extends JpaRepository<Details_Model, Long> {
    Details_Model findByOrderID(String orderID);
    Optional<Details_Model> findFirstByOrderID(String orderID);
}