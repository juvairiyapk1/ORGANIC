package com.timeco.application.Repository;

import com.timeco.application.model.category.Category;
import com.timeco.application.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByProductNameContaining(String searchTerm);

    Optional<Product> findById(Long id);

    List<Product> findByCategory(Category vegetablesCategory);



//    @Query("SELECT p FROM Product p WHERE p.category.isLocked = false")
//    List<Product> findProductsByUnlockedCategories();
}



//    @Query(value = "SELECT p.*, i.image_data FROM product p JOIN product_image i ON p.id = i.product_id", nativeQuery = true)
//    List<ProductImageDto> getAllProductsWithImages();

