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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;

@Service
public class CartService {

    private List<CartItem> cartItems;
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductService productService;

    @Autowired
    private CartItemRepository cartItemRepository;

    public CartService(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }


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

//    private Cart createCart(User user) {
//        // Check if a cart already exists for the user
//        Cart existingCart = cartRepository.findByUser(user);
//
//        if (existingCart != null) {
//            // A cart already exists, you can return the existing cart
//            return existingCart;
//        } else {
//            // Create a new cart for the user
//            Cart cart = new Cart(user);
//            return cartRepository.save(cart);
//        }
//    }

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


    public Map<String, Object> updateCartItemQuantity(Long cartItemId, int quantityChange) {
        Map<String, Object> response = new HashMap<>();

        CartItem cartItem = cartItemRepository.findById(cartItemId).orElse(null);

        if (cartItem != null) {
            int currentQuantity = cartItem.getQuantity();
            int updatedQuantity = currentQuantity + quantityChange;

            int pQuantity=cartItem.getProduct().getQuantity();

            if (updatedQuantity >= 1 && updatedQuantity<=pQuantity) {
                cartItem.setQuantity(updatedQuantity);
                cartItemRepository.save(cartItem);


                response.put("success", true);
                response.put("updatedQuantity", updatedQuantity);
                response.put("updatedTotalPrice", cartItem.getPrice() * updatedQuantity);
                // Calculate and update sub-total and total amounts
                response.put("subTotal", calculateSubTotal(cartItems));
        //        response.put("totalAmount", calculateTotalAmount(cartItems,));
            } else {
                response.put("success", false);
            }
        } else {
            response.put("success", false);
        }

        return response;
    }

    // Calculate sub-total
    public double calculateSubTotal(List<CartItem> cartItems) {
        double subTotal = 0.0;
        for (CartItem cartItem : cartItems) {
            subTotal += cartItem.getPrice() * cartItem.getQuantity();
        }
        return subTotal;
    }

    public double calculateTotalAmount(List<CartItem> cartItems, double discount, double shippingCost) {
        // Calculate sub-total first as a double
        double totalPrice = calculateSubTotal(cartItems);

        // Apply discount if provided
        if (discount > 0) {
            totalPrice -= discount;
        }

        // Add shipping cost if provided
        if (shippingCost > 0) {
            totalPrice += shippingCost;
        }

        return totalPrice;
    }

//    public int getCartItemCount(Principal principal) {
//        if (principal != null)
//        {
//            String user=principal.getName();
//            Cart cart=user.getCart();
//            if (cart != null)
//            {
//                return cart.getCartItems().size();
//            }
//
//
//        }
//
//    return 0;
//    }
}


//    public void deleteAllProduct(User user) {
//        cartItemRepository.deleteByUser(user);
//    }

