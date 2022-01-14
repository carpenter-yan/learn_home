package base.spring.service.impl;

import base.spring.domain.User;
import base.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {
    @Autowired
    private User user;

    @Override
    public void printUser(){
        System.out.println(user.getId() + ":" + user.getUserName());
    }
}
