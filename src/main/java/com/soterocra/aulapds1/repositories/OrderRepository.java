package com.soterocra.aulapds1.repositories;

import com.soterocra.aulapds1.entities.Order;
import com.soterocra.aulapds1.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByClient(User client);

}
