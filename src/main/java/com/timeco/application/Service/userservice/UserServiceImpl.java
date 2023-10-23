package com.timeco.application.Service.userservice;

import com.timeco.application.Dto.RegistrationDto;
import com.timeco.application.Repository.RoleRepository;
import com.timeco.application.Repository.UserRepository;
import com.timeco.application.model.role.Role;
import com.timeco.application.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


//    public UserServiceImpl(UserRepository userRepository) {
//        super();
//        this.userRepository = userRepository;
//    }





    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Email received in loadUserByUsername: " + email);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        if(user.isBlocked()){
            throw new DisabledException("User is blocked");
        }
        System.out.println("User found: " + user.getEmail());
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));

    }


    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }


    @Override
    @Transactional
    public User save(RegistrationDto registrationDto) {
        //check if the user with provided already exist
        User existingUser=userRepository.findByEmail(registrationDto.getEmail());
        if(existingUser!=null){
            throw new RuntimeException("user with this same email already exist.");

        }
        User user = new User(registrationDto.getFirstName(),
                registrationDto.getLastName(), registrationDto.getEmail(),
                registrationDto.getPhoneNumber(), passwordEncoder.encode(registrationDto.getPassword()),
                Arrays.asList(new Role("ROLE_USER")));
        return user;

    }
    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void lockUser(Long id) {
        User lockCustomer = userRepository.findById(id).get();
        lockCustomer.setBlocked(true);
        userRepository.save(lockCustomer);
    }

    @Override
    public void unlockUser(Long id) {
        User lockCustomer = userRepository.findById(id).get();
        lockCustomer.setBlocked(false);
        userRepository.save(lockCustomer);
    }
    @Override
    public List<User> getUsersByPartialEmailOrName(String searchTerm) {
        return userRepository.findByEmailContainingOrFirstNameContaining(searchTerm, searchTerm);


    }


}

