package webprog.services.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import webprog.entity.Product;
import webprog.repository.ProductRepository;
import webprog.services.IProductService;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product save(Product entity) {
        if (entity.getProductId() == null) {
            return productRepository.save(entity);
        } else {
            Optional<Product> opt = findById(entity.getProductId());
            if (opt.isPresent()) {
                if (!StringUtils.hasText(entity.getImages())) {
                    entity.setImages(opt.get().getImages());
                }
                if (entity.getCreateDate() == null) {
                    entity.setCreateDate(opt.get().getCreateDate());
                }
            }
            return productRepository.save(entity);
        }
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public List<Product> findByProductNameContaining(String name) {
        return productRepository.findByProductNameContaining(name);
    }

    @Override
    public Page<Product> findByProductNameContaining(String name, Pageable pageable) {
        return productRepository.findByProductNameContaining(name, pageable);
    }

    @Override
    public Optional<Product> findByProductName(String name) {
        return productRepository.findByProductName(name);
    }

    @Override
    public Optional<Product> findByCreateDate(Date createAt) {
        return productRepository.findByCreateDate(createAt);
    }

    @Override
    public Page<Product> findByCategoryId(Long categoryId, Pageable pageable) {
        return productRepository.findByCategory_CategoryId(categoryId, pageable);
    }

    @Override
    public Page<Product> search(String name, Long categoryId, Pageable pageable) {
        boolean hasName = StringUtils.hasText(name);
        boolean hasCategory = categoryId != null && categoryId > 0;

        if (hasName && hasCategory) {
            return productRepository.findByProductNameContainingAndCategory_CategoryId(name, categoryId, pageable);
        } else if (hasName) {
            return productRepository.findByProductNameContaining(name, pageable);
        } else if (hasCategory) {
            return productRepository.findByCategory_CategoryId(categoryId, pageable);
        } else {
            return productRepository.findAll(pageable);
        }
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public void delete(Product entity) {
        productRepository.delete(entity);
    }

    @Override
    public long count() {
        return productRepository.count();
    }
}

