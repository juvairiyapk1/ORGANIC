package com.timeco.application.web.usercontrollers;


import com.timeco.application.Service.productservice.ProductService;
import com.timeco.application.model.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String listProductsWithImages(Model model) {
        List<Product> productsWithImages = productService.getAllProducts();

        model.addAttribute("productsWithImages", productsWithImages);

        return "Userproduct";
    }

    @GetMapping("/products/{productId}")
    public String viewProductDetails(@PathVariable Long productId, Model model) {
        // Retrieve the product details for the specified productId
       Product product = productService.getProductById(productId);

        if (product !=null) {
            model.addAttribute("product", product);
            return "product-single"; // Create a Thymeleaf template for product details.
        } else {
            // Handle the case where the product with the specified ID does not exist.
            return "redirect:/user/Userproduct";
        }
    }


}
