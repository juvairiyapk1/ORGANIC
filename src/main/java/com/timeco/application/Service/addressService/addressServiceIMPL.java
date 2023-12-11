package com.timeco.application.Service.addressService;

import com.timeco.application.Dto.AddressDto;
import com.timeco.application.Repository.AddressRepository;
import com.timeco.application.Repository.UserRepository;
import com.timeco.application.model.user.Address;
import com.timeco.application.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class addressServiceIMPL implements AddressService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;
    public Address saveAddress(AddressDto addressDto, Principal principal){
        String userName=principal.getName();
        User user=userRepository.findByEmail(userName);
        Address address1=new Address();
        address1.setUser(user);
        address1.setFirstName(addressDto.getFirstName());
        address1.setLastName(addressDto.getLastName());
        address1.setEmail(addressDto.getEmail());
        address1.setStreet(addressDto.getStreet());
        address1.setCity(addressDto.getCity());
        address1.setState(addressDto.getState());
        address1.setAddress(addressDto.getAddress());
        address1.setPostalCode(addressDto.getPostalCode());
        address1.setPhoneNumber(addressDto.getPhoneNumber());
        return addressRepository.save(address1);

    }
   @Transactional
    @Override
    public Address updateAddress(Long addressId, AddressDto addressDto) {

        Address existAddress = addressRepository.findById(addressId).orElse(null);
        existAddress.setFirstName(addressDto.getFirstName());
        existAddress.setLastName(addressDto.getLastName());
        existAddress.setEmail(addressDto.getEmail());
        existAddress.setStreet(addressDto.getCity());
        existAddress.setState(addressDto.getState());
        existAddress.setAddress(addressDto.getAddress());
        existAddress.setPostalCode(addressDto.getPostalCode());
        existAddress.setPhoneNumber(addressDto.getPhoneNumber());
        System.out.println(addressDto.getFirstName());
        return addressRepository.save(existAddress);
    }

    @Override
    public void deleteAddress(Long addressId) {
        Optional<Address> optionalAddress=addressRepository.findById(addressId);
        if (optionalAddress.isPresent())
        {
            addressRepository.deleteById(addressId);
        }
    }


    @Override
    public Address getAddress(Long addressId){
        return addressRepository.findById(addressId).orElseThrow(()-> new  NoSuchElementException("Address not found with id"+addressId));
    }
}
