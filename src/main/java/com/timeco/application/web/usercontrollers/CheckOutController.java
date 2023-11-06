package com.timeco.application.web.usercontrollers;

import com.timeco.application.Dto.AddressDto;
import com.timeco.application.Repository.AddressRepository;
import com.timeco.application.Repository.UserRepository;
import com.timeco.application.Service.addressService.AddressService;
import com.timeco.application.model.user.Address;
import com.timeco.application.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class CheckOutController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressService addressService;

    @Autowired
    private AddressRepository addressRepository;

    @GetMapping("/checkOut")
    public String showCheckOut(Model model, Principal principal) {
        String userName = principal.getName();
        User user = userRepository.findByEmail(userName);
        List<Address> addresses = addressRepository.findByUser(user);
        model.addAttribute("addresses", addresses);
        return "checkout";
    }

    @PostMapping("/addAddress")
    public String addAddress(@ModelAttribute("address")AddressDto addressDto, Principal principal) {

        addressService.saveAddress(addressDto,principal);

        return "redirect:/checkOut"; // Redirect to the user's profile page
    }
}
