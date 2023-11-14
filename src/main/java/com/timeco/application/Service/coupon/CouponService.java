package com.timeco.application.Service.coupon;

import com.timeco.application.Dto.CouponDto;
import com.timeco.application.model.order.Coupon;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CouponService {

    List<Coupon> getAllCoupons();

    Coupon getCouponById(Integer couponId);

//    void saveCoupon(Coupon coupon);


    void addCoupon(CouponDto couponDto);

    void lockCoupon(Integer id);

    void unlockCoupon(Integer id);

    void editCouponById(Integer couponId, CouponDto editCoupon);
}
