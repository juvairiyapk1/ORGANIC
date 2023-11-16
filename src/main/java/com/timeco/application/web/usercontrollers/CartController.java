package com.timeco.application.web.usercontrollers;

import com.timeco.application.Dto.ProductDto;
import com.timeco.application.Repository.CartItemRepository;
import com.timeco.application.Repository.ProductRepository;
import com.timeco.application.Repository.UserRepository;
import com.timeco.application.Service.cartService.CartService;
import com.timeco.application.Service.productservice.ProductService;
import com.timeco.application.model.cart.Cart;
import com.timeco.application.model.cart.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartItemRepository cartItemRepository;


//    adding product to the cart -----------------------------------------------------------
@PostMapping("/addProductToCart")
public ResponseEntity<String> addToCart(@RequestBody ProductDto productDTO, Principal principal) {
    if (principal != null) {
        try {
            // Check if the product is already in the cart
            boolean isProductInCart = cartService.isProductInCart(productDTO, principal);

            if (isProductInCart) {
                // Product is already in the cart, return a message
                return ResponseEntity.ok("Product is already in the cart.");
            } else {
                // Check if the user has reached the limit for this product
                int productLimit = 5; // Set your limit here
                int productCountInCart = cartService.getProductCountInCart(productDTO, principal);

                if (productCountInCart >= productLimit) {
                    return ResponseEntity.ok("Product limit reached. You cannot add more of this product.");
                }

                // Product is not in the cart and within the limit, so you can add it
                cartService.addProductToCart(productDTO, principal);
                return ResponseEntity.ok("Product added to cart.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add product to cart.");
        }
    } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in.");
    }
}

    @GetMapping("/cart")
    public String showCartPage(Model model, Principal principal) {
        List<CartItem> cartItems = cartService.getCartItemsForUser(principal);
//        double discount=50;
        double deliveryCharge=100;
        double total = cartService.calculateTotalAmount(cartItems,deliveryCharge);
        double subTotal=cartService.calculateSubTotal(cartItems);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice",total);
        model.addAttribute("subTotal",subTotal);

        return "cart"; // Return the view name for the cart page (cart.html)
    }

    @GetMapping("/updateCartItemQuantity/{cartItemId}")
    @ResponseBody
    public Map<String, Object> updateCartItemQuantity(@PathVariable Long cartItemId, @RequestParam int quantityChange) {

        return cartService.updateCartItemQuantity(cartItemId, quantityChange);
    }
    @PostMapping("/deleteCartItem/{cartItemId}")
    public String deleteProduct(@PathVariable Long cartItemId) {

            cartService.deleteProduct(cartItemId);
            return "redirect:/cart";

    }



        }
