package com.timeco.application.web.admincontrollers;

import com.timeco.application.Dto.AddressDto;
import com.timeco.application.Dto.CouponDto;
import com.timeco.application.Dto.ProductDto;
import com.timeco.application.Repository.CouponRepository;
import com.timeco.application.Service.coupon.CouponService;
import com.timeco.application.model.order.Coupon;
import com.timeco.application.model.order.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminCouponController {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponService couponService;

    @GetMapping("/couponList")
    public String showOrderList(Model model){
        List<Coupon> coupons=couponRepository.findAll();
        model.addAttribute("coupons",coupons);
        return "couponList";
    }

    @PostMapping("/add/couponList")
    public String addCoupon(@ModelAttribute("coupon") CouponDto couponDto,Long userId)
    {
        System.out.println("55555555555555"+couponDto.getPercentage());
        couponService.addCoupon(couponDto,userId);

        return "redirect:/admin/couponList";
    }

    @GetMapping("/blockCoupon/{id}")
    public String listCoupon(@PathVariable Integer id) {

        couponService.lockCoupon(id);

        return "redirect:/admin/couponList";
    }

    @GetMapping("/unblockCoupon/{id}")
    public String unlistCategory(@PathVariable Integer id) {

        couponService.unlockCoupon(id);

        return "redirect:/admin/couponList";
    }

    @PostMapping("/editCoupon/{couponId}")
    public String updateCoupon(@PathVariable Integer couponId,@ModelAttribute("coupon") CouponDto couponDto)
    {
        couponService.updateCoupon(couponId,couponDto);

        return "redirect:/admin/couponList";
    }

}
