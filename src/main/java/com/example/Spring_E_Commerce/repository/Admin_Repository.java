package com.example.Spring_E_Commerce.repository;

import com.example.Spring_E_Commerce.model.Admin_Model;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Admin_Repository extends JpaRepository<Admin_Model, Long> {
    Optional<Admin_Model> findByEmailAndPassword(String email, String password);
}
