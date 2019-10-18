package com.soterocra.aulapds1.services;

import com.soterocra.aulapds1.dto.CategoryDTO;
import com.soterocra.aulapds1.dto.ProductCategoriesDTO;
import com.soterocra.aulapds1.dto.ProductDTO;
import com.soterocra.aulapds1.entities.Category;
import com.soterocra.aulapds1.entities.Product;
import com.soterocra.aulapds1.repositories.CategoryRepository;
import com.soterocra.aulapds1.repositories.ProductRepository;
import com.soterocra.aulapds1.services.exceptions.DatabaseException;
import com.soterocra.aulapds1.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    public Page<ProductDTO> findAllPaged(Pageable pageable) {
        Page<Product> list = repository.findAll(pageable);
        return list.map(ProductDTO::new);
    }

    public ProductDTO findById(Long id) {
        Optional<Product> obj = repository.findById(id);
        Product entity = obj.orElseThrow(() -> new ResourceNotFoundException(id));
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO insert(ProductCategoriesDTO dto) {
        Product entity = dto.toEntity();
        setProductCategories(entity, dto.getCategories());
        entity = repository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductCategoriesDTO dto) {
        try {
            Product entity = repository.getOne(id);
            updateData(entity, dto);
            entity = repository.save(entity);
            return new ProductDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    private void setProductCategories(Product entity, List<CategoryDTO> categories) {
        entity.getCategories().clear();
        categories.forEach(e -> {
            Category category = categoryRepository.getOne(e.getId());
            entity.getCategories().add(category);
        });
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    private void updateData(Product entity, ProductCategoriesDTO dto) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());
        if (dto.getCategories() != null && dto.getCategories().size() > 0) {
            setProductCategories(entity, dto.getCategories());
        }
    }

    @Transactional
    public Page<ProductDTO> findByCategoryPaged(Long categoryId, Pageable pageable) {
        Category category = categoryRepository.getOne(categoryId);
        Page<Product> products = repository.findByCategory(category, pageable);
        return products.map(ProductDTO::new);
    }

    @Transactional
    public void addCategory(Long id, CategoryDTO dto) {
        Product product = repository.getOne(id);
        Category category = categoryRepository.getOne(dto.getId());
        product.getCategories().add(category);
        repository.save(product);
    }

    @Transactional
    public void removeCategory(Long id, CategoryDTO dto) {
        Product product = repository.getOne(id);
        Category category = categoryRepository.getOne(dto.getId());
        product.getCategories().remove(category);
        repository.save(product);
    }

    @Transactional
    public void setCategories(Long id, List<CategoryDTO> dto) {
        Product product = repository.getOne(id);
        setProductCategories(product, dto);
        repository.save(product);
    }
}
