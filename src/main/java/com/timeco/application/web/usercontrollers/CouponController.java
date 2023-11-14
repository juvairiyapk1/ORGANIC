package com.timeco.application.web.usercontrollers;

import com.timeco.application.Dto.CouponDto;
import com.timeco.application.Service.coupon.CouponService;
import com.timeco.application.model.order.Coupon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CouponController {

    @Autowired
    private CouponService couponService;


    @GetMapping("/coupons")
    public String showCoupon(Model model){
        model.addAttribute("couponCode",couponService.getAllCoupons());
        return "coupon";
    }



}
