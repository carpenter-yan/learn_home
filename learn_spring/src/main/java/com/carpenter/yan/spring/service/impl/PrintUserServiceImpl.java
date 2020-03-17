package com.carpenter.yan.spring.service.impl;

import com.carpenter.yan.spring.domain.User;
import com.carpenter.yan.spring.service.PrintUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PrintUserServiceImpl implements PrintUserService {
    @Autowired
    private User user;

    @Override
    public void printUser(){
        System.out.println(user.getId() + ":" + user.getUserName());
    }
}
