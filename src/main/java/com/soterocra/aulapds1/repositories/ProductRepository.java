package com.soterocra.aulapds1.repositories;

import com.soterocra.aulapds1.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
