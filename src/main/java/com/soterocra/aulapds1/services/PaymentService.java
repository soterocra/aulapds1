package com.soterocra.aulapds1.services;

import com.soterocra.aulapds1.dto.CategoryDTO;
import com.soterocra.aulapds1.dto.PaymentDTO;
import com.soterocra.aulapds1.entities.Category;
import com.soterocra.aulapds1.entities.Payment;
import com.soterocra.aulapds1.repositories.CategoryRepository;
import com.soterocra.aulapds1.repositories.PaymentRepository;
import com.soterocra.aulapds1.repositories.ProductRepository;
import com.soterocra.aulapds1.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository repository;

    public List<PaymentDTO> findAll() {
        List<Payment> list = repository.findAll();
        return list.stream().map(PaymentDTO::new).collect(Collectors.toList());
    }

    public PaymentDTO findById(Long id) {
        Optional<Payment> obj = repository.findById(id);
        Payment entity = obj.orElseThrow(() -> new ResourceNotFoundException(id));
        return new PaymentDTO(entity);
    }

}
