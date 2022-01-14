package base.spring.service.impl;

import base.spring.domain.Role;
import base.spring.service.RoleService;
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
