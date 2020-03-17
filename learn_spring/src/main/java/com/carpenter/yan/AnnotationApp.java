package com.carpenter.yan;

import com.carpenter.yan.spring.domain.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AnnotationApp {
    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(JavaConfig.class);
        User user = ctx.getBean("user", User.class);
        System.out.println(user.getUserName());
    }
}
