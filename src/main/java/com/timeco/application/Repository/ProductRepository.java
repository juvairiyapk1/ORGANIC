package com.timeco.application.Repository;

import com.timeco.application.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByProductNameContaining(String searchTerm);

    Optional<Product> findById(Long id);


//    @Query(value = "SELECT p.*, i.image_data FROM product p JOIN product_image i ON p.id = i.product_id", nativeQuery = true)
//    List<ProductImageDto> getAllProductsWithImages();
}
