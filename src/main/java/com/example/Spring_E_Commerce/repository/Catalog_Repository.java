//package com.example.Spring_E_Commerce.repository;
//
//import com.example.Spring_E_Commerce.model.Catalog_Model;
//import org.springframework.data.jpa.repository.JpaRepository;
//import java.util.Optional;
//
//public interface Catalog_Repository extends JpaRepository<Catalog_Model, Long> {
//    Optional<Catalog_Model> findByUnum(String unum);
//}
package com.example.Spring_E_Commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Spring_E_Commerce.model.Catalog_Model;
import java.util.Optional;

// Replace Long with your Catalog_Model’s primary key type
public interface Catalog_Repository extends JpaRepository<Catalog_Model, Long> {
    Optional<Catalog_Model> findByUnum(String unum);
}

