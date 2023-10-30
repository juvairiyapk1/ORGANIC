package com.timeco.application.Service.productservice;

import com.timeco.application.Dto.ProductDto;
import com.timeco.application.model.product.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {

//    public Product addProduct(Product product);
    Product addProduct(Product product);

    public void updateProductById(Long id,ProductDto product);

    public void deleteProductById(Long id);
    public List<Product> getAllProducts();

    List<Product> searchProducts(String searchTerm);

    Product getProductById(Long productId);

    boolean isExistOrNot(Long id);

//    void deleteProduct(Long productId) throws Exception;



}

