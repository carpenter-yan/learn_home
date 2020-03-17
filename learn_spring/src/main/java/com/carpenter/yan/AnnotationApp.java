package com.carpenter.yan;

import com.carpenter.yan.config.JavaConfig;
import com.carpenter.yan.spring.domain.Role;
import com.carpenter.yan.spring.domain.User;
import com.carpenter.yan.spring.service.PrintUserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

public class AnnotationApp {
    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(JavaConfig.class);
        User user = ctx.getBean("user", User.class);
        System.out.println(user.getUserName());

        PrintUserService printUserService = ctx.getBean(PrintUserService.class);
        printUserService.printUser();

        Map<String, String> config = ctx.getBean("config", Map.class);
        System.out.println(config.get("key"));

        Role role = ctx.getBean("role", Role.class);
        System.out.println(role.getRoleName());
    }
}
