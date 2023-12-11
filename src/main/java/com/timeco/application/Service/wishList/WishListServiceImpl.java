package com.timeco.application.Service.wishList;

import com.timeco.application.Dto.ProductDto;
import com.timeco.application.Repository.ProductRepository;
import com.timeco.application.Repository.UserRepository;
import com.timeco.application.Repository.WishListRepository;
import com.timeco.application.Service.productservice.ProductService;
import com.timeco.application.Service.userservice.UserService;
import com.timeco.application.model.cart.CartItem;
import com.timeco.application.model.order.WishList;
import com.timeco.application.model.product.Product;
import com.timeco.application.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class WishListServiceImpl implements WishListService {


    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;


    @Override
    public void saveWishList(WishList wishList) {
        wishListRepository.save(wishList);

    }

    @Override
    public void addProduct(Long productId, WishList wishList) {
        Product product = productService.getProductById(productId);
        product.getWishList().add(wishList);
        productService.addProduct(product);
        wishList.getProducts().add(product);
        wishListRepository.save(wishList);
    }

    @Override
    public Set<Product> getWishListByUser(Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        if (user != null) {
            WishList wishList = user.getWishlist();
            if (wishList != null) {
                return wishList.getProducts();
            }

        }
        return new HashSet<>();
    }

    @Override
    public void deleteProductFromWishList(Long productId, WishList wishList) {
        Optional<Product> product = productRepository.findById(productId);

        if (product.isPresent()) {
            // Remove the product from the wish list
            wishList.removeProduct(product.get());
            wishListRepository.save(wishList);
        }
    }

    @Override
    public boolean isProductInWishList(Long productId, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        WishList wishList = user.getWishlist();

        return wishList != null && wishList.getProducts().stream()
                .anyMatch(product -> product.getId().equals(productId));
    }

    @Override
    public int getProductCountInWishList(Long productId, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        WishList wishList = user.getWishlist();

        long count = 0;
        if (wishList != null) {
            count = wishList.getProducts().stream()
                    .filter(product -> product.getId().equals(productId))
                    .count();
        }

        return (int) count;
    }

    @Override
    public void addProductToWishList(Long productId, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        WishList wishList = user.getWishlist();

        Product product = productRepository.findById(productId).orElse(null);

        if (wishList == null) {
            wishList = new WishList();
            wishList.setUser(user);
            wishListRepository.save(wishList);
        }

        if (product != null) {
            wishList.addProduct(product);
            wishListRepository.save(wishList);
        }
    }

}