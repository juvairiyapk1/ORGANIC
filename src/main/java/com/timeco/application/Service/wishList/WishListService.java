package com.timeco.application.Service.wishList;

import com.timeco.application.Dto.ProductDto;
import com.timeco.application.model.order.WishList;
import com.timeco.application.model.product.Product;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Set;

@Service
public interface WishListService {


    public  void saveWishList(WishList wishList);


    void addProduct(Long productId, WishList wishList);

    Set<Product> getWishListByUser(Principal principal);



    void deleteProductFromWishList(Long productId, WishList wishList);

    boolean isProductInWishList(Long productId, Principal principal);


    int getProductCountInWishList(Long productId, Principal principal);

    void addProductToWishList(Long productId, Principal principal);
}
