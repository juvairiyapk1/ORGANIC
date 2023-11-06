package com.timeco.application.web.usercontrollers;

import com.timeco.application.Dto.AddressDto;
import com.timeco.application.Repository.AddressRepository;
import com.timeco.application.Repository.UserRepository;
import com.timeco.application.Service.addressService.AddressService;
import com.timeco.application.Service.userservice.UserService;
import com.timeco.application.model.user.Address;
import com.timeco.application.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserService userService;
    @GetMapping("/address")
    public String showAddresses(Model model, Principal principal) {
        String userName = principal.getName();
        User user = userRepository.findByEmail(userName);
        List<Address> addresses = addressRepository.findByUser(user);
        model.addAttribute("addresses", addresses);
        return "address";
    }


    @GetMapping("/addAddress")
    public String showAddAddressForm(Model model) {
        AddressDto addressDto = new AddressDto();
        model.addAttribute("address", addressDto);
        return "addAddress"; // Create an HTML template for the add address form
    }

    @PostMapping("/add-address")
    public String addAddress(@ModelAttribute("address")AddressDto addressDto, Principal principal) {

        addressService.saveAddress(addressDto,principal);

        return "redirect:/userProfile"; // Redirect to the user's profile page
    }
    @PostMapping("/editAddress/{addressId}")
    public String editUserAddress(@PathVariable Long addressId,AddressDto addressDto)
    {
      addressService.updateAddress(addressId,addressDto);

      return "redirect:/address";
    }
    @PostMapping("/deleteAddress/{addressId}")
    public String deleteAddress(@PathVariable Long addressId)
    {
        addressService.deleteAddress(addressId);
        return "redirect:/address";
    }

}
