package com.timeco.application.Service.productservice;

import antlr.TokenStreamRewriteEngine;
import com.timeco.application.Dto.ProductDto;
import com.timeco.application.Repository.CartItemRepository;
import com.timeco.application.Repository.CategoryRepository;
import com.timeco.application.Repository.ProductRepository;
import com.timeco.application.model.cart.CartItem;
import com.timeco.application.model.category.Category;
import com.timeco.application.model.product.Product;
import org.aspectj.apache.bcel.generic.InstructionList;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

   @Autowired
   private CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public Product addProduct(Product product) {

        return productRepository.save(product);
    }

    @Override
    @Transactional
    public void updateProductById(Long id, ProductDto products) {

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

                // Set the Category on the Product
                product.setCategory(category.get());
                productRepository.save(product);

            }
        }

        // Save the updated Product
    }

    @Transactional
    @Override
    public void deleteProductById(Long id) {

        this.productRepository.deleteById(id);

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



//    @Transactional
//    @Override
//    public void deleteProductWithCartItems(Long productId) {
//        Optional<Product> productOptional = productRepository.findById(productId);
//
//        if (productOptional.isPresent()) {
//            Product product = productOptional.get();
//
//            // Retrieve the related cart items
//            Set<CartItem> cartItems = product.getCartItems();
//
//
//            // Finally, delete the product
//            productRepository.delete(product);
//        }
//    }


//    @Override
//    public void deleteProduct(Long productId) throws Exception {
//        try {
//            productRepository.deleteById(productId);
//        }
//        catch (Exception e)
//        {
//            throw new Exception("Failed to delete product");
//        }
//
//    }


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

//    @Override
//    public void save(Product product) {
//        productRepository.save(product);
//    }

}
