package com.timeco.application.Service.coupon;

import com.timeco.application.Dto.CouponDto;
import com.timeco.application.Repository.CartItemRepository;
import com.timeco.application.Repository.CartRepository;
import com.timeco.application.Repository.CouponRepository;
import com.timeco.application.Repository.UserRepository;
import com.timeco.application.Service.cartService.CartService;
import com.timeco.application.model.cart.Cart;
import com.timeco.application.model.cart.CartItem;
import com.timeco.application.model.order.Coupon;
import com.timeco.application.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Service
public class CouponServiceImpl implements CouponService{


    @Autowired
    private CartService cartService;
    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private  CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    @Override
    public Coupon getCouponById(Integer couponId) {
        return couponRepository.findById(couponId).orElse(null);
    }


    @Override
    @Transactional
    public void addCoupon(CouponDto couponDto,Long userId) {

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
    public Coupon updateCoupon(Integer couponId, CouponDto couponDto) {


//		write logic for editing the coupon
            Coupon editCoupon = couponRepository.findById(couponId).orElse(null);
//        editCoupon.setCartItemsCount(coupon.getCartItemsCount());
            editCoupon.setCouponCode(couponDto.getCouponCode());
            editCoupon.setDescription(couponDto.getDescription());
            editCoupon.setExpiryDate(couponDto.getExpiryDate());
            editCoupon.setActive(couponDto.isActive());
            editCoupon.setUsageCount(couponDto.getUsageCount());
            editCoupon.setMinimumPurchaseAmount(couponDto.getMinimumPurchaseAmount());
            editCoupon.setPercentage(couponDto.getPercentage());
            return couponRepository.save(editCoupon);


    }

    @Override
    public double findByDiscount(String couponCode, Principal principal) {

        Coupon coupon = couponRepository.findCouponByCouponCode(couponCode);
        double couponDiscount=0;

        double deliveryCharge=100;
        if(coupon != null)
        {
            double percentage=coupon.getPercentage();
            String userName=principal.getName();
            User user=userRepository.findByEmail(userName);
            Cart cart=cartRepository.findByUser(user);
            List<CartItem> cartItem=cartItemRepository.findByCart(cart);
            double total=cartService.calculateTotalAmount(cartItem,deliveryCharge);
            couponDiscount=(total*percentage)/100;

        }

        return couponDiscount;

    }

    @Override
    public double newTotal(@RequestParam("couponCode") String couponCode, Principal principal) {
        double shippingCost = 100;
        Cart cart = cartRepository.findByUser(userRepository.findByEmail(principal.getName()));

        if (cart != null) {
            List<CartItem> cartItems = cartItemRepository.findByCart(cart);
            double cartTotal = cartService.calculateTotalAmount(cartItems, shippingCost);
            double couponDiscount = findByDiscount(couponCode, principal);
            cartTotal -= couponDiscount;
            return cartTotal;
        }

        return 0.0;
    }


//    @Override
//    public Boolean isCouponValid(String couponCode, double orderAmount)
//    {
//        Coupon coupon=couponRepository.findByCode(couponCode);
//
//        if (coupon != null && coupon.isActive() && orderAmount>=coupon.getMinimumPurchaseAmount()){
//            return  true;
//        }
//
//      return false;
//    }


}
