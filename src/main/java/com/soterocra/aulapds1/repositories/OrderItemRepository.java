package com.soterocra.aulapds1.repositories;

import com.soterocra.aulapds1.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
