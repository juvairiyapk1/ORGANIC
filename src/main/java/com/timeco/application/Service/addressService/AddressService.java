package com.timeco.application.Service.addressService;

import com.timeco.application.Dto.AddressDto;
import com.timeco.application.model.user.Address;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public interface AddressService {
    Address saveAddress(AddressDto addressDto, Principal principal);

    Address updateAddress(Long addressId, AddressDto addressDto);

    void deleteAddress(Long addressId);


//    Address getAddressById(Long addressId);
}
