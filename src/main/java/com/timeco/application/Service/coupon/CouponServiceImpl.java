package com.timeco.application.Service.coupon;

import com.timeco.application.Dto.CouponDto;
import com.timeco.application.Repository.CouponRepository;
import com.timeco.application.model.category.Category;
import com.timeco.application.model.order.Coupon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class CouponServiceImpl implements CouponService{

    @Autowired
    private CouponRepository couponRepository;
    @Override
    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    @Override
    public Coupon getCouponById(Integer couponId) {
        return couponRepository.findById(couponId).orElse(null);
    }

//    @Override
//    public void saveCoupon(Coupon coupon) {
//        couponRepository.save(coupon);
//    }

    @Override
    @Transactional
    public void addCoupon(CouponDto couponDto) {

        Coupon coupon=new Coupon();
        coupon.setCouponCode(couponDto.getCouponCode());
        coupon.setDescription(couponDto.getDescription());
        coupon.setExpiryDate(couponDto.getExpiryDate());
        coupon.setPercentage(couponDto.getPercentage());
        coupon.setMinimumPurchaseAmount(couponDto.getMinimumPurchaseAmount());
        coupon.setUsageCount(couponDto.getUsageCount());
        couponRepository.save(coupon);
    }

    @Override
    public void lockCoupon(Integer couponId) {
        Coupon lockCoupon = couponRepository.findById(couponId).get();
        lockCoupon.setActive(true);
        couponRepository.save(lockCoupon);
    }

    @Override
    public void unlockCoupon(Integer couponId ){

        Coupon lockCoupon = couponRepository.findById(couponId).get();
        System.out.println("6666666666666666666666666666666"+couponRepository.findById(couponId));
        lockCoupon.setActive(false);
        couponRepository.save(lockCoupon);

    }

    @Override
    public void editCouponById(Integer couponId, CouponDto editCoupon) {

        Coupon coupon=couponRepository.findById(couponId).orElse(null);
        if(coupon != null)
        {
            coupon.setCouponCode(editCoupon.getCouponCode());
            coupon.setDescription(editCoupon.getDescription());
            coupon.setExpiryDate(editCoupon.getPercentage());
        }
    }


}
