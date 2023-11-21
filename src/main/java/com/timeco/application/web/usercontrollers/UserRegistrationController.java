package com.timeco.application.web.usercontrollers;


import com.timeco.application.Dto.RegistrationDto;
import com.timeco.application.Repository.UserRepository;
import com.timeco.application.Service.otpservice.OtpService;
import com.timeco.application.Service.userservice.UserService;
import com.timeco.application.model.cart.Cart;
import com.timeco.application.model.order.Wallet;
import com.timeco.application.model.user.LoginDto;
import com.timeco.application.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/registration")

public class UserRegistrationController {

    @Autowired
    private UserService userService;

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
    public String register(@ModelAttribute("user") RegistrationDto registrationDto, HttpSession session,Model model)
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

//        if(LoginAccount.getOtp() == null){
//            System.out.println("hgddbxvbfgfsfsfsfsfsfdsf");
//            redirectAttributes.addFlashAttribute("message","Please fill in field");
//        }
            String emailId = session.getAttribute("validEmailId").toString();
            boolean flag = otpService.validateRegistrationOtp(emailId, LoginAccount.getOtp());

            int otpTimer = (int) session.getAttribute("otpTimer");

            if (flag) {
                User verifyCustomer = (User) session.getAttribute("verifyCustomer");
                Cart cart = new Cart();
                cart.setUser(verifyCustomer);
                verifyCustomer.setCart(cart);
                Wallet wallet=new Wallet();
                wallet.setUser(verifyCustomer);
                verifyCustomer.setWallet(wallet);
                userRepository.save(verifyCustomer);

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


    @PostMapping("/resendOTP")
    public String resendOTP(HttpSession session) {
        String email = session.getAttribute("validEmailId").toString();
        otpService.sendRegistrationOtp(email);
        return "redirect:/registration/otpVerification";
    }




}
