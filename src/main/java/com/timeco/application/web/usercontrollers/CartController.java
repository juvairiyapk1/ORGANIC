package com.timeco.application.web.usercontrollers;

import com.timeco.application.Dto.ProductDto;
import com.timeco.application.Repository.CartItemRepository;
import com.timeco.application.Repository.ProductRepository;
import com.timeco.application.Repository.UserRepository;
import com.timeco.application.Service.cartService.CartService;
import com.timeco.application.Service.productservice.ProductService;
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
//    @PostMapping("/decrementQuantity")
//    public ResponseEntity<Integer> decrementQuantity(@RequestParam Long cartItemId) {
//        int newQuantity = cartService.decrementQuantity(cartItemId);
//        return ResponseEntity.ok(newQuantity);
//    }
//
//    @PostMapping("/incrementQuantity")
//    public ResponseEntity<Integer> incrementQuantity(@RequestParam Long cartItemId) {
//        int newQuantity = cartService.incrementQuantity(cartItemId);
//        return ResponseEntity.ok(newQuantity);
//    }
//    @PostMapping("/updateTotal")
//    public ResponseEntity<Double> updateTotal(@RequestParam Long cartItemId) {
//        double updatedPrice = cartService.updateTotal(cartItemId);
//
//        return ResponseEntity.ok(updatedPrice);
//    }
//
    @GetMapping("/cart")
    public String showCartPage(Model model, Principal principal) {
        List<CartItem> cartItems = cartService.getCartItemsForUser(principal);
        model.addAttribute("cartItems", cartItems);
        return "cart"; // Return the view name for the cart page (cart.html)
    }
//    @PostMapping("/updateQuantity")
//    public ResponseEntity<String> updateQuantity(@RequestParam int quantity,Long cartItemId) {
//        // Assume you have a CartItem class and a CartItemRepository
//        CartItem cartItem = cartService.getCartItemById(cartItemId);
//
//        if (cartItem != null) {
//            int currentQuantity = cartItem.getQuantity();
//            int newQuantity = currentQuantity + quantity;
//
//            // Ensure the quantity doesn't go below 1
//            if (newQuantity < 1) {
//                newQuantity = 1;
//            }
//
//            cartItem.setQuantity(newQuantity);
//
//            // Calculate the updated total price
//            double totalPrice = cartItem.getPrice() * newQuantity;
//
//            // Save the updated cart item to the repository
//            cartService.saveCartItem(cartItem);
//
//            // Calculate the total price for the cart
//            double totalCartPrice = cartService.calculateTotalCartPrice();
//
//            // You can return the updated quantity and total price as JSON
//            return ResponseEntity.ok("{ \"updatedQuantity\": " + newQuantity + ", \"totalPrice\": " + totalPrice + " }");
//        } else {
//            return ResponseEntity.badRequest().body("Cart item not found");
//        }
//    }

    @PostMapping("/updateCartItemQuantity/{cartItemId}")
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
