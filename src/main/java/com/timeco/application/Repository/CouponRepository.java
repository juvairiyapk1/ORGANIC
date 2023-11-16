package com.timeco.application.Repository;

import com.timeco.application.model.order.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon,Integer> {


//    String findByCouponCode(String couponCode);

    Coupon findCouponByCouponCode(String couponCode);
}
