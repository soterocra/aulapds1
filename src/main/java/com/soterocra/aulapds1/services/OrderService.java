package com.soterocra.aulapds1.services;

import com.soterocra.aulapds1.dto.OrderDTO;
import com.soterocra.aulapds1.entities.Order;
import com.soterocra.aulapds1.entities.User;
import com.soterocra.aulapds1.repositories.OrderRepository;
import com.soterocra.aulapds1.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private AuthService authService;

    public List<OrderDTO> findAll() {
        List<Order> list = repository.findAll();
        return list.stream().map(OrderDTO::new).collect(Collectors.toList());
    }

    public OrderDTO findById(Long id) {
        Optional<Order> obj = repository.findById(id);
        Order entity = obj.orElseThrow(() -> new ResourceNotFoundException(id));
        authService.validateOwnOrderOrAdmin(entity);
        return new OrderDTO(entity);
    }

    public List<OrderDTO> findByClient() {
        User client = authService.authenticated();
        List<Order> list = repository.findByClient(client);
        return list.stream().map(OrderDTO::new).collect(Collectors.toList());
    }
}
