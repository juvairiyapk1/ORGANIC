package com.timeco.application.web.usercontrollers;

import com.timeco.application.Dto.ProductDto;
import com.timeco.application.Repository.WishListRepository;
import com.timeco.application.Service.userservice.UserService;
import com.timeco.application.Service.wishList.WishListService;
import com.timeco.application.model.order.WishList;
import com.timeco.application.model.product.Product;
import com.timeco.application.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
public class WishListController {

    @Autowired
    private WishListService wishListService;

    @Autowired
    private UserService userService;

    @Autowired
    private WishListRepository wishListRepository;


    @GetMapping("/showWishlist")
    public String showWishList(Model model,Principal principal){
        Set<Product> wishList=wishListService.getWishListByUser(principal);
        model.addAttribute("wishList",wishList);
        return "WishList";
    }
    @PostMapping("/addProductToWishList")
    public ResponseEntity<String> addToWishList(@RequestParam Long id, Principal principal) {
        if (principal != null) {
            try {
                boolean isProductInWishList = wishListService.isProductInWishList(id, principal);

                if (isProductInWishList) {
                    return ResponseEntity.ok("Product is already in the wishlist.");
                } else {
                    int productLimit = 5; // Set your limit here
                    int productCountInWishList = wishListService.getProductCountInWishList(id, principal);

                    if (productCountInWishList >= productLimit) {
                        return ResponseEntity.ok("Product limit reached. You cannot add more of this product.");
                    }

                    wishListService.addProductToWishList(id, principal);
                    return ResponseEntity.ok("Product added to wishlist.");
                }
            } catch (Exception e) {
                e.printStackTrace(); // Log the exception for debugging
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add product to wishlist.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in.");
        }
    }


    @PostMapping("/delete/{productId}")
    public String deleteProduct(@PathVariable Long productId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findUserByEmail(userName);

        WishList wishList = user.getWishlist();


        wishListService.deleteProductFromWishList(productId, wishList);

        return "redirect:/showWishlist";
    }
}
