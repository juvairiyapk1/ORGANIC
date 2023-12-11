package com.timeco.application.Service.productservice;

import com.timeco.application.Dto.ProductDto;
import com.timeco.application.model.category.Category;
import com.timeco.application.model.product.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import java.io.IOException;
import java.util.List;

@Service
public interface ProductService {

//    public Product addProduct(Product product);
    Product addProduct(Product product);

    public void updateProductById(Long id, ProductDto product, MultipartFile file) throws IOException;


    public List<Product> getAllProducts();

    List<Product> searchProducts(String searchTerm);

    Product getProductById(Long productId);

    boolean isExistOrNot(Long id);





    void lockProduct(Long id);

    void unlockProduct(Long id);



}

