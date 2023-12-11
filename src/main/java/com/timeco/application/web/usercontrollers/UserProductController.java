package com.timeco.application.web.usercontrollers;


import com.timeco.application.Repository.CategoryRepository;
import com.timeco.application.Service.categoryservice.CategoryService;
import com.timeco.application.Service.productservice.ProductService;
import com.timeco.application.model.category.Category;
import com.timeco.application.model.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/products/{productId}")
    public String viewProductDetails(@PathVariable Long productId, Model model) {
        // Retrieve the product details for the specified productId
        Product product = productService.getProductById(productId);

        if (product != null) {
            model.addAttribute("product", product);
            return "product-single"; // Create a Thymeleaf template for product details.
        } else {
            // Handle the case where the product with the specified ID does not exist.
            return "redirect:/user/Userproduct";
        }

    }


    @GetMapping("/searchProduct")
    public String searchProducts(@RequestParam("searchTerm") String searchTerm, Model model) {
        List<Product> products = productService.searchProducts(searchTerm);
        model.addAttribute("productsWithImages", products);

        return "Userproduct";
    }

    @GetMapping("/products")
    public String listProductsWithImages(@RequestParam(name = "category", required = false) Long categoryId, Model model) {
        List<Product> productsWithImages;

        if (categoryId != null) {
            Category selectedCategory = categoryService.getCategoryById(categoryId);
            if (selectedCategory != null) {
                List<Product> categoryProducts = selectedCategory.getProducts();
                // Filter out blocked products
                categoryProducts = categoryProducts.stream()
                        .filter(product -> !product.getBlocked()) // Assuming a method like isBlocked() is present in your Product entity
                        .collect(Collectors.toList());
                model.addAttribute("categoryProducts", categoryProducts);
                model.addAttribute("selectedCategoryId", categoryId);
            }
        } else {
            // Get all products and filter out blocked ones
            productsWithImages = productService.getAllProducts().stream()
                    .filter(product -> !product.getBlocked()) // Assuming a method like isBlocked() is present in your Product entity
                    .collect(Collectors.toList());
            model.addAttribute("productsWithImages", productsWithImages);
        }

        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("categories", categories);

        return "Userproduct";
    }




}
