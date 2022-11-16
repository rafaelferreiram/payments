package com.payment.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.payment.entity.Payment;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, Integer>{

}
