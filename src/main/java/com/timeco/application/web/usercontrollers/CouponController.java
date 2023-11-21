package com.timeco.application.web.usercontrollers;

import com.timeco.application.Dto.CouponDto;
import com.timeco.application.Repository.CartItemRepository;
import com.timeco.application.Repository.CartRepository;
import com.timeco.application.Repository.CouponRepository;
import com.timeco.application.Repository.UserRepository;
import com.timeco.application.Service.cartService.CartService;
import com.timeco.application.Service.coupon.CouponService;
import com.timeco.application.model.cart.Cart;
import com.timeco.application.model.cart.CartItem;
import com.timeco.application.model.order.Coupon;
import com.timeco.application.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class CouponController {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CartService cartService;
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @GetMapping("/coupons")
    public String showCoupon(Model model){
        List<Coupon>coupons=couponService.getAllCoupons();
        model.addAttribute("couponCode",coupons);
        return "coupon";
    }

    @GetMapping("/couponValidation")
    public ResponseEntity<Map<String, Object>> couponValidation(@RequestParam("couponCode") String couponCode, Principal principal) {
        Map<String, Object> response = new HashMap<>();

        double deliveryCharge = 100;
        LocalDate currentDate = LocalDate.now();

        Coupon coupon = couponRepository.findCouponByCouponCode(couponCode);

        if (coupon != null) {
            String userName = principal.getName();
            User user = userRepository.findByEmail(userName);

            // Check if the user has already used the coupon
            if (user.getCoupons().contains(coupon)) {
                response.put("valid", false);
                response.put("message", "Coupon has already been used by this user.");
                return ResponseEntity.ok(response);
            }

            Cart cart = cartRepository.findByUser(user);
            List<CartItem> cartItems = cartItemRepository.findByCart(cart);
            double couponDiscount = couponService.findByDiscount(couponCode, principal);
            double total = cartService.calculateTotalAmount(cartItems, deliveryCharge);

            if (!coupon.isActive() && total >= coupon.getMinimumPurchaseAmount() && currentDate.isBefore(coupon.getExpiryDate())) {
                double discountedTotal = total - couponDiscount;
                // Add the coupon details to the response
                response.put("valid", true);
                response.put("discountedTotal", discountedTotal);
                response.put("discountAmount", couponDiscount);
                response.put("couponCode",couponCode);
//                user.getCoupons().add(coupon);
//                coupon.getUsers().add(user);
//
//                userRepository.save(user);
//                couponRepository.save(coupon);

                return ResponseEntity.ok(response);
            } else {
                // Coupon is not valid
                response.put("valid", false);
                response.put("message", "Invalid coupon. Check expiration date or minimum purchase amount.");
                return ResponseEntity.ok(response);
            }
        } else {
            // Coupon not found
            response.put("valid", false);
            response.put("message", "Invalid coupon code.");
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/removeCoupon")
    public ResponseEntity<Map<String, Object>> removeCoupon(@RequestParam("couponCode") String couponCode, Principal principal) {
        Map<String, Object> response = new HashMap<>();

        try {
            String userName = principal.getName();
            User user = userRepository.findByEmail(userName);

            // Find the coupon by code
            Coupon coupon = couponRepository.findCouponByCouponCode(couponCode);

            if (coupon != null && user.getCoupons().contains(coupon)) {
                // Remove the specific coupon from the user
                user.getCoupons().remove(coupon);
                userRepository.save(user);

                response.put("success", true);
                response.put("message", "Coupon removed successfully");
                response.put("newTotal", couponService.newTotal(couponCode,principal));

            } else {
                response.put("success", false);
                response.put("message", "Coupon not found or not associated with the user");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error removing coupon, please try again");
        }

        return ResponseEntity.ok(response);
    }





}
