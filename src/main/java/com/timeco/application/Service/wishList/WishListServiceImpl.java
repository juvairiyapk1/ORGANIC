package com.timeco.application.Service.wishList;

import com.timeco.application.Repository.UserRepository;
import com.timeco.application.Repository.WishListRepository;
import com.timeco.application.Service.productservice.ProductService;
import com.timeco.application.model.order.WishList;
import com.timeco.application.model.product.Product;
import com.timeco.application.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class WishListServiceImpl implements WishListService{


    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private ProductService productService;


    @Override
    public  void saveWishList(WishList wishList) {
        wishListRepository.save(wishList);

    }

    @Override
    public void addProduct(Long productId, WishList wishList) {
        Product product=productService.getProductById(productId);
        product.getWishList().add(wishList);
        productService.addProduct(product);
        wishList.getProducts().add(product);
        wishListRepository.save(wishList);
    }

    @Override
    public void delete(Long productId, WishList wishlist) {
        Product product=productService.getProductById(productId);
        product.getWishList().remove(wishlist);
        productService.addProduct(product);
        wishlist.getProducts().remove(product);
        wishListRepository.save(wishlist);

    }
}
