package com.timeco.application.web.usercontrollers;

import com.timeco.application.Dto.AddressDto;
import com.timeco.application.Repository.AddressRepository;
import com.timeco.application.Repository.PaymentMethodRepository;
import com.timeco.application.Repository.UserRepository;
import com.timeco.application.Service.addressService.AddressService;
import com.timeco.application.Service.cartService.CartService;
import com.timeco.application.Service.purchaseOrder.PurchaseOrderService;
import com.timeco.application.model.cart.CartItem;
import com.timeco.application.model.order.PaymentMethod;
import com.timeco.application.model.order.PurchaseOrder;
import com.timeco.application.model.user.Address;
import com.timeco.application.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
public class CheckOutController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressService addressService;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    public CartService cartService;

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @GetMapping("/checkOut")
    public String showCheckOut(Model model, Principal principal) {
        String userName = principal.getName();
        User user = userRepository.findByEmail(userName);
        List<Address> addresses = addressRepository.findByUser(user);
        List<CartItem> cartItems = cartService.getCartItemsForUser(principal);
        double discount=50;
        double deliveryCharge=100;
        double total = cartService.calculateTotalAmount(cartItems,discount,deliveryCharge);
        double subTotal=cartService.calculateSubTotal(cartItems);
        model.addAttribute("addresses", addresses);
        model.addAttribute("total",total);
        model.addAttribute("subTotal",subTotal);
        return "checkout";
    }

    @PostMapping("/addAddress")
    public String addAddress(@ModelAttribute("address")AddressDto addressDto, Principal principal) {

        addressService.saveAddress(addressDto,principal);

        return "redirect:/checkOut"; // Redirect to the user's profile page
    }


}
