package com.timeco.application.Service.coupon;

import com.timeco.application.Dto.CouponDto;
import com.timeco.application.model.order.Coupon;
import com.timeco.application.model.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
public interface CouponService {

    List<Coupon> getAllCoupons();

    Coupon getCouponById(Integer couponId);

//    void saveCoupon(Coupon coupon);



    @Transactional
    void addCoupon(CouponDto couponDto,Long userId);

    void lockCoupon(Integer id);

    void unlockCoupon(Integer id);


    Coupon updateCoupon(Integer couponId, CouponDto couponDto);

    double findByDiscount(String couponCode, Principal principal);



//    Boolean isCouponValid(String couponCode, double orderAmount);


}
