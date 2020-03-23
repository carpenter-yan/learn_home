package com.carpenter.yan.spring.service.impl;

import com.carpenter.yan.spring.domain.Role;
import com.carpenter.yan.spring.service.RoleService;
import com.sun.deploy.util.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class RoleServiceImpl implements RoleService {
    @Override
    public void printRole(Role role) {
        System.out.println(StringUtils.join(Arrays.asList(String.valueOf(role.getId()), role.getRoleName(), role.getNote()), ":"));
    }
}
