package com.timeco.application.Service.userservice;


import com.timeco.application.Dto.AddressDto;
import com.timeco.application.Dto.RegistrationDto;
import com.timeco.application.model.product.Product;
import com.timeco.application.model.user.Address;
import com.timeco.application.model.user.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;

@Service
public interface UserService extends UserDetailsService {


    @Transactional
    User save(RegistrationDto registrationDto);

    User findUserByEmail(String email);
    public void lockUser(Long id);
    public void unlockUser(Long id);


    List<User> getUsersByPartialEmailOrName(String searchTerm);


    @Transactional
    boolean updateProfile(User updatedUser);

    public boolean isCurrentPasswordCorrect(String email, String currentPassword);
    public void updatePassword(String email, String newPassword);


}