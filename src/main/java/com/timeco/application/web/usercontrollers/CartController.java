package com.timeco.application.web.usercontrollers;

import com.timeco.application.Dto.ProductDto;
import com.timeco.application.Repository.ProductRepository;
import com.timeco.application.Repository.UserRepository;
import com.timeco.application.Service.cartService.CartService;
import com.timeco.application.model.cart.Cart;
import com.timeco.application.model.cart.CartItem;
import com.timeco.application.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.List;


@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;



    @PostMapping("/addProductToCart")
    public ResponseEntity <String> addToCart(@RequestBody ProductDto productDTO, Principal principal) {


        try {
            // Check if the product is already in the cart
            boolean isProductInCart = cartService.isProductInCart(productDTO, principal);


            if (isProductInCart ) {
                // Product is already in the cart, return a message
                return ResponseEntity.ok("Product is already in the cart.");
            } else {
                // Product is not in the cart, so you can add it
                System.out.println(productDTO.getProductName());
                cartService.addProductToCart(productDTO, principal);
                return ResponseEntity.ok("Product added to cart.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add product to cart.");
        }

    }

    @GetMapping("/cart")
    public String showCartPage(Model model, Principal principal) {
        List<CartItem> cartItems = cartService.getCartItemsForUser(principal);
        System.out.println("cccccccccccccccc"+cartItems.isEmpty());
        model.addAttribute("cartItems", cartItems);
        return "cart"; // Return the view name for the cart page (cart.html)
    }


}
