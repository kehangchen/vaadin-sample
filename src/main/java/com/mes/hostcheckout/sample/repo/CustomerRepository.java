package com.mes.hostcheckout.sample.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mes.hostcheckout.sample.model.Customer;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	List<Customer> findByLastNameStartsWithIgnoreCase(String lastName);
}
