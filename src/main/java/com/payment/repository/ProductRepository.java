package com.payment.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.payment.entity.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer>{

}
