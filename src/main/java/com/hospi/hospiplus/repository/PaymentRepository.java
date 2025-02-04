package com.hospi.hospiplus.repository;

import com.hospi.hospiplus.model.Payment;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<Payment, Integer> {
}
