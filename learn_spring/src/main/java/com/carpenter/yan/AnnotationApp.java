package com.carpenter.yan;

import com.carpenter.yan.config.JavaConfig;
import com.carpenter.yan.spring.domain.Life;
import com.carpenter.yan.spring.domain.Role;
import com.carpenter.yan.spring.domain.User;
import com.carpenter.yan.spring.service.RoleService;
import com.carpenter.yan.spring.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

public class AnnotationApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(JavaConfig.class);
        System.out.println("============================================================");
        User user = ctx.getBean("user", User.class);
        System.out.println(user.getUserName());

        UserService userService = ctx.getBean(UserService.class);
        userService.printUser();

        Map<String, String> config = ctx.getBean("config", Map.class);
        System.out.println(config.get("key"));

        Role role = ctx.getBean("role", Role.class);
        System.out.println(role.getRoleName());

        Life life = ctx.getBean("life", Life.class);
        System.out.println(life.getName());

        RoleService roleService = ctx.getBean(RoleService.class);
        Role r = new Role();
        r.setId(1L);
        r.setRoleName("工程师");
        r.setNote("技术人才");
        roleService.printRole(r);
        //roleService.printRole(null);

        ctx.close();
    }
}
