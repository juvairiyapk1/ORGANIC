package com.timeco.application.Service.cartService;

import com.timeco.application.Dto.ProductDto;
import com.timeco.application.Repository.CartItemRepository;
import com.timeco.application.Repository.CartRepository;
import com.timeco.application.Repository.UserRepository;
import com.timeco.application.Service.productservice.ProductService;
import com.timeco.application.model.cart.Cart;
import com.timeco.application.model.cart.CartItem;
import com.timeco.application.model.product.Product;
import com.timeco.application.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.*;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductService productService;

    @Autowired
    private CartItemRepository cartItemRepository;


    public boolean isProductInCart(ProductDto productDTO, Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        if (user != null) {
            Product product = productService.getProductById(productDTO.getId());

            if (product !=null) {
                Cart cart = user.getCart();
                if (cart != null) {
                    for (CartItem item : cart.getCartItems()) {
                        if (item.getProduct().equals(product)) {
                            return true; // Product is already in the cart
                        }
                    }
                }
            }
        }

        return false; // Product is not in the cart
    }

    private Cart createCart(User user) {
        // Check if a cart already exists for the user
        Cart existingCart = cartRepository.findByUser(user);

        if (existingCart != null) {
            // A cart already exists, you can return the existing cart
            return existingCart;
        } else {
            // Create a new cart for the user
            Cart cart = new Cart(user);
            return cartRepository.save(cart);
        }
    }

    private double calculateTotalCartPrice(Cart cart) {
        double totalPrice = 0.0;
        for (CartItem item : cart.getCartItems()) {
            totalPrice += item.getPrice();
        }
        return totalPrice;
    }

    @Transactional
    public void addProductToCart(ProductDto productDTO, Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        if (user != null) {
                if(user != null){
                    Cart cart =user.getCart();


                if (cart == null) {
                    cart= new Cart();
                    cart.setUser(user);
                    user.setCart(cart);
                }


                // Create a new CartItem
                CartItem cartItem = new CartItem();
                cartItem.setProduct(productService.getProductById(productDTO.getId()));
                cartItem.setQuantity(productDTO.getQuantity());
                cartItem.setCart(cart);
                cartItem.setPrice(cartItem.getProduct().getPrice() * productDTO.getQuantity());

                // Add the CartItem to the cart
                cart.getCartItems().add(cartItem);
                // Update the total price for the cart
                cartRepository.save(cart);
            }
        }

    }

    public List<CartItem> getCartItemsForUser(Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        if (user != null) {
            Cart cart = user.getCart();
            if (cart != null) {
                return cart.getCartItems();
            }
        }
        return new ArrayList<>(); // Return an empty list if no items are found.
    }


    public int getProductCountInCart(ProductDto productDTO, Principal principal) {
        if (principal != null)
        {
            String userName=principal.getName();
            User user=userRepository.findByEmail(userName);
            Cart cart=cartRepository.findByUser(user);
            List<CartItem>cartItems=cartItemRepository.findByCart(cart);
            if (cartItems != null) {
                Set<Long> uniqueProductIds = new HashSet<>();

                for (CartItem cartItem : cartItems) {
                    uniqueProductIds.add(cartItem.getProduct().getId());
                }

                int uniqueProductCount = uniqueProductIds.size();

                return uniqueProductCount;

            }
        }
        return 0;
    }

    public void deleteProduct(Long cartItemId) {
      Optional<CartItem> cartItem=cartItemRepository.findById(cartItemId);
      if (cartItem.isPresent())
      {
          cartItemRepository.deleteById(cartItemId);
      }

    }
}
