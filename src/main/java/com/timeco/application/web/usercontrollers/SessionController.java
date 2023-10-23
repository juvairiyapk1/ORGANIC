package com.timeco.application.web.usercontrollers;

import com.timeco.application.Repository.CustomSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {

    @Autowired
    CustomSessionRepository customSessionRepository;
}
