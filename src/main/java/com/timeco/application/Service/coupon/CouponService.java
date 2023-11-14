package com.timeco.application.Service.coupon;

import com.timeco.application.Dto.CouponDto;
import com.timeco.application.model.order.Coupon;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface CouponService {

    List<Coupon> getAllCoupons();

    Coupon getCouponById(Integer couponId);

//    void saveCoupon(Coupon coupon);



    @Transactional
    void addCoupon(CouponDto couponDto);

    void lockCoupon(Integer id);

    void unlockCoupon(Integer id);

    Coupon updateCoupon(Coupon coupon);

//    void editCouponById(Integer couponId, CouponDto editCoupon);

//    Coupon updateCoupon(Coupon coupon);
}
