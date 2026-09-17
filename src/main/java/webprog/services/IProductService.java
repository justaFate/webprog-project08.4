package webprog.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import webprog.entity.Product;

public interface IProductService {
    Product save(Product entity);
    Optional<Product> findById(Long id);
    List<Product> findAll();
    Page<Product> findAll(Pageable pageable);
    List<Product> findByProductNameContaining(String name);
    Page<Product> findByProductNameContaining(String name, Pageable pageable);
    Optional<Product> findByProductName(String name);
    Optional<Product> findByCreateDate(Date createAt);
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    Page<Product> search(String name, Long categoryId, Pageable pageable);
    void deleteById(Long id);
    void delete(Product entity);
    long count();
}

