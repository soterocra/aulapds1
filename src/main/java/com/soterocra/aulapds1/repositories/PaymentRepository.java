package com.soterocra.aulapds1.repositories;

import com.soterocra.aulapds1.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
