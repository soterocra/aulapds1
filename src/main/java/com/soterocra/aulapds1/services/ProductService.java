package com.soterocra.aulapds1.services;

import com.soterocra.aulapds1.dto.ProductDTO;
import com.soterocra.aulapds1.entities.Product;
import com.soterocra.aulapds1.repositories.ProductRepository;
import com.soterocra.aulapds1.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public List<ProductDTO> findAll() {
        List<Product> list = repository.findAll();
        return list.stream().map(ProductDTO::new).collect(Collectors.toList());
    }

    public ProductDTO findById(Long id) {
        Optional<Product> obj = repository.findById(id);
        Product entity = obj.orElseThrow(() -> new ResourceNotFoundException(id));
        return new ProductDTO(entity);
    }

}
