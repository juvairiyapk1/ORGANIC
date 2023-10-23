package com.timeco.application.Service.userservice;


import com.timeco.application.Dto.RegistrationDto;
import com.timeco.application.model.product.Product;
import com.timeco.application.model.user.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService extends UserDetailsService {

    User save(RegistrationDto registrationDto);

    User findUserByEmail(String email);
    public void lockUser(Long id);
    public void unlockUser(Long id);


    List<User> getUsersByPartialEmailOrName(String searchTerm);





}