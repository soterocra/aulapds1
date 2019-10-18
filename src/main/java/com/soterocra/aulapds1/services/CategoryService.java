package com.soterocra.aulapds1.services;

import com.soterocra.aulapds1.dto.CategoryDTO;
import com.soterocra.aulapds1.entities.Category;
import com.soterocra.aulapds1.entities.Product;
import com.soterocra.aulapds1.repositories.CategoryRepository;
import com.soterocra.aulapds1.repositories.ProductRepository;
import com.soterocra.aulapds1.services.exceptions.DatabaseException;
import com.soterocra.aulapds1.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Autowired
    private ProductRepository productRepository;

    public List<CategoryDTO> findAll() {
        List<Category> list = repository.findAll();
        return list.stream().map(CategoryDTO::new).collect(Collectors.toList());
    }

    public CategoryDTO findById(Long id) {
        Optional<Category> obj = repository.findById(id);
        Category entity = obj.orElseThrow(() -> new ResourceNotFoundException(id));
        return new CategoryDTO(entity);
    }

    public CategoryDTO insert(CategoryDTO dto) {
        Category entity = dto.toEntity();
        entity = repository.save(entity);
        return new CategoryDTO(entity);
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

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {
        try {
            Category entity = repository.getOne(id);
            updateData(entity, dto);
            entity = repository.save(entity);
            return new CategoryDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(Category entity, CategoryDTO dto) {
        entity.setName(dto.getName());
    }

    @Transactional(readOnly = true)
    public List<CategoryDTO> findByProduct(Long productId) {
        Product product = productRepository.getOne(productId);
        Set<Category> set = product.getCategories();
        return set.stream().map(CategoryDTO::new).collect(Collectors.toList());
    }
}
