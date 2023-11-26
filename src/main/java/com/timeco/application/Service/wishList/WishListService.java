package com.timeco.application.Service.wishList;

import com.timeco.application.model.order.WishList;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public interface WishListService {


    public  void saveWishList(WishList wishList);


    void addProduct(Long productId, WishList wishList);

    void delete(Long productId, WishList wishlist);
}
