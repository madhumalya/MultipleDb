package com.example.Postgres.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Postgres.Entitie.Product;

public interface ProductRepo extends JpaRepository<Product, Integer> {

	
}
