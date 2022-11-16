package com.payment.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.payment.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer>{

}
