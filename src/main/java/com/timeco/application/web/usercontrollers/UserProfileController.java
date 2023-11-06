package com.timeco.application.web.usercontrollers;

import com.timeco.application.Dto.RegistrationDto;
import com.timeco.application.Repository.AddressRepository;
import com.timeco.application.Repository.UserRepository;
import com.timeco.application.Service.addressService.AddressService;
import com.timeco.application.Service.userservice.UserService;
import com.timeco.application.model.user.Address;
import com.timeco.application.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class UserProfileController {

    @Autowired
    private UserService userService;
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/userProfile")
    public String profileView(Model model,Principal principal)
    {
        String userName=principal.getName();
        User user=userRepository.findByEmail(userName);
        model.addAttribute("user",user);
        boolean editMode = false; // Set it to either true or false based on your logic
        model.addAttribute("editMode", editMode);


        return "UserProfile";
    }

    @PostMapping("/userProfile")
    public String updateUserProfile(@ModelAttribute User user,Model model){
        if (userService.updateProfile(user)){
            model.addAttribute("successMessage","Profile updated successfully");
        }
        else {
            model.addAttribute("errorMessage", "Failed to update profile.");
        }
        return "redirect:/userProfile";
    }

    @GetMapping("/editProfile")
    public String enableEdit(Model model,Principal principal)
    {
        String userName= principal.getName();
        User user=userRepository.findByEmail(userName);
        model.addAttribute("user", user);
        model.addAttribute("editMode", true);
        return "UserProfile";
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam String currentPassword, String newPassword, String confirmPassword,Principal principal,Model model)
    {
        String currentUser=principal.getName();

        // Check if the current password is correct
        if (!userService.isCurrentPasswordCorrect(currentUser, currentPassword)) {
            model.addAttribute("error","Current password is incorrect.");
            return "redirect:/userProfile";
        }

        // Check if the new password and confirm password match
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error","New password and confirm password do not match.");
            return "redirect:/userProfile";
        }

        // Update the password
        userService.updatePassword(currentUser,newPassword);

        model.addAttribute("success","Password updated successfully.");
        return "redirect:/userProfile";

    }



}
