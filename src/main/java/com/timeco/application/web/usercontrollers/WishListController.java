package com.timeco.application.web.usercontrollers;

import com.timeco.application.Service.userservice.UserService;
import com.timeco.application.Service.wishList.WishListService;
import com.timeco.application.model.order.WishList;
import com.timeco.application.model.product.Product;
import com.timeco.application.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Controller
public class WishListController {

    @Autowired
    private WishListService wishListService;

    @Autowired
    private UserService userService;

    @GetMapping("/wishList")
    public String showWishList(Principal principal, HttpSession session, Model model, @RequestParam("id")Long productId)
    {
        session.removeAttribute("emptyWishList");
        String userName=principal.getName();
        User user=userService.findUserByEmail(userName);
        WishList wishList=user.getWishlist();
        if (wishList == null)
        {
            wishList =new WishList();
            wishList.setUser(user);
            wishListService.saveWishList(wishList);

        }
        if(productId !=0)
        {
            wishListService.addProduct(productId,wishList);
        }
        model.addAttribute("wishList",wishList);
        return "WishList";

    }

    @GetMapping("/wishList/delete")
    public String deleteWishList(@RequestParam("id")Long productId,Principal principal)
    {
        String userName=principal.getName();
        User user=userService.findUserByEmail(userName);
        if(user==null)
        {
            throw new UsernameNotFoundException("User did not found");
        }

        wishListService.delete(productId,user.getWishlist());
        return "redirect:/wishList";

    }
}
