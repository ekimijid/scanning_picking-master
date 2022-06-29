package com.essers.scanning.data.service;

import com.essers.scanning.data.model.Product;
import com.essers.scanning.data.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public final class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void save(String name, String description, String price) {
        productRepository.save(new Product(0L, name, description, price));
    }
}
