package com.timeco.application.Service.productservice;

import com.timeco.application.Dto.ProductDto;
import com.timeco.application.Repository.CartItemRepository;
import com.timeco.application.Repository.CategoryRepository;
import com.timeco.application.Repository.OrderItemRepository;
import com.timeco.application.Repository.ProductRepository;
import com.timeco.application.model.cart.CartItem;
import com.timeco.application.model.category.Category;
import com.timeco.application.model.order.Coupon;
import com.timeco.application.model.order.OrderItem;
import com.timeco.application.model.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.timeco.application.web.admincontrollers.AdminProductController.UPLOAD_DIR;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

   @Autowired
   private CartItemRepository cartItemRepository;

   @Autowired
   private OrderItemRepository orderItemRepository;

    @Override
    @Transactional
    public Product addProduct(Product product) {

        return productRepository.save(product);
    }

    @Override
    @Transactional
    public void updateProductById(Long id, ProductDto products, MultipartFile file) throws IOException {

        // Retrieve the existing Product from the database
        Product product = productRepository.findById(id).orElse(null);

        // Retrieve the Category based on categoryId from the ProductDto
        Optional<Category> category = categoryRepository.findById(products.getCategory().getId());
        if (category.isPresent()) {

            if (product != null) {
                product.setProductName(products.getProductName());
                product.setDescription(products.getDescription());
                product.setCurrent_state(products.getCurrent_state());
                product.setPrice(products.getPrice());
                product.setQuantity(products.getQuantity());
                byte[] imageBytes = file.getBytes();
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR, fileName);
                Files.write(filePath, imageBytes);

                // Set the new image information in the Product entity
                product.setProductImages(fileName);


                // Set the Category on the Product
                product.setCategory(category.get());
                productRepository.save(product);

            }
        }

        // Save the updated Product
    }


    @Override
    public List<Product> getAllProducts(){

        return productRepository.findAll();
    }

    @Override
    public List<Product> searchProducts(String searchTerm) {

       return productRepository.findByProductNameContaining(searchTerm);
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElse(null);
    }





    @Override
    public boolean isExistOrNot(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if(productOptional.isPresent())
        {
            CartItem cartItem = cartItemRepository.findByProduct(productOptional.get());
            if(cartItem != null)
            {
                return true;
            }
            return false;
        }
        return false;
    }



    @Override
    public void lockProduct(Long id) {
        Product lockProduct = productRepository.findById(id).get();
        lockProduct.setBlocked(true);
        productRepository.save(lockProduct);
    }

    @Override
    public void unlockProduct(Long id) {
        Product unLockProduct=productRepository.findById(id).get();
        unLockProduct.setBlocked(false);
        productRepository.save(unLockProduct);

    }


}
