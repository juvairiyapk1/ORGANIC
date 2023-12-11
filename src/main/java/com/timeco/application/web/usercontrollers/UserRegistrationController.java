package com.timeco.application.web.usercontrollers;


import com.timeco.application.Dto.RegistrationDto;
import com.timeco.application.Repository.UserRepository;
import com.timeco.application.Repository.WalletRepository;
import com.timeco.application.Service.otpservice.OtpService;
import com.timeco.application.Service.userservice.UserService;
import com.timeco.application.Service.wallet.WalletService;
import com.timeco.application.model.cart.Cart;
import com.timeco.application.model.order.Wallet;
import com.timeco.application.model.user.LoginDto;
import com.timeco.application.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.UUID;

@Controller
@RequestMapping("/registration")

public class UserRegistrationController {


    @Autowired
    private WalletService walletService;
    @Autowired
    private UserService userService;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private OtpService otpService;

    @Autowired
    private UserRepository userRepository;

    public UserRegistrationController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("user")
    public RegistrationDto registrationDto(){
        return new RegistrationDto();
    }


    @GetMapping("/RegForm")
    public String RegForm(){

        return "registration";

    }

    @PostMapping("/Register")
    public String register(@ModelAttribute("user") RegistrationDto registrationDto, HttpSession session,Model model,
                           @RequestParam(name ="ref",required = false)String referralCode)
    {

        User existingUser = userService.findUserByEmail(registrationDto.getEmail());
        if (existingUser != null) {
            // Add an error message and redirect back to the registration form
            model.addAttribute("error", "Existed email ID");
            return "registration"; // Replace 'registration-form' with the actual URL of your registration form.
        }


        User verifyCustomer = userService.save(registrationDto);
        otpService.sendRegistrationOtp(verifyCustomer.getEmail());
        session.setAttribute("validEmailId", verifyCustomer.getEmail());
        session.setAttribute("verifyCustomer", verifyCustomer);

        if (referralCode != null && !referralCode.isEmpty()) {
            session.setAttribute("referralCode", referralCode);
            System.out.println("refefral=============="+referralCode);
        }

        return "redirect:/registration/otpVerification?success";
    }

    @GetMapping("/otpVerification")
    public String otpVerification(Model model, HttpSession session) {

       session.setAttribute("otpTimer",120);

        LoginDto otpBasedLoginAccount = new LoginDto();
        model.addAttribute("otpBasedLoginAccount", otpBasedLoginAccount);
        return "userValidation";
    }



    @PostMapping("/otpRegistrationValidation")
    public String otpRegistrationValidation (@ModelAttribute("otpBasedLoginAccount") LoginDto LoginAccount, HttpSession session, RedirectAttributes redirectAttributes) {

      String emailId = session.getAttribute("validEmailId").toString();
            boolean flag = otpService.validateRegistrationOtp(emailId, LoginAccount.getOtp());

            int otpTimer = (int) session.getAttribute("otpTimer");

            if (flag) {
                User verifyCustomer = (User) session.getAttribute("verifyCustomer");
                String referralCode=generateRefferralCode();
                verifyCustomer.setReferralCode(referralCode);

                Cart cart = new Cart();
                cart.setUser(verifyCustomer);
                verifyCustomer.setCart(cart);

                Wallet wallet=new Wallet();
                wallet.setUser(verifyCustomer);
                verifyCustomer.setWallet(wallet);



                userRepository.save(verifyCustomer);


                String referral= (String) session.getAttribute("referralCode");
                User referrer=userRepository.findByReferralCode(referral);
                if(referral != null && !referral.isEmpty() && referrer != null) {



                    double referralBonusAmount = 100.0;
                    walletService.addAmountToWallet(verifyCustomer, referralBonusAmount);
                    walletService.addAmountToWallet(referrer,referralBonusAmount);


                }


                return "redirect:/";
            } else {
                redirectAttributes.addFlashAttribute("error", "Invalid otp. Please try again");

                if (otpTimer <= 0) {
                    redirectAttributes.addFlashAttribute("otpTimer", "Time exceeded. Please regenerate OTP.");
                } else {
                    otpTimer -= 1;
                    session.setAttribute("otpTimer", otpTimer);
                }

                return "redirect:/registration/otpVerification";
            }


    }

    private String generateRefferralCode() {
        return UUID.randomUUID().toString().substring(0,8);
    }


    @PostMapping("/resendOTP")
    public String resendOTP(HttpSession session) {
        String email = session.getAttribute("validEmailId").toString();
        otpService.sendRegistrationOtp(email);
        return "redirect:/registration/otpVerification";
    }




}
