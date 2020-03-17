package com.carpenter.yan;

import com.carpenter.yan.spring.domain.Role;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class XmlApp
{
    public static void main( String[] args )
    {
        System.out.println( "-----------begin------------" );
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
        Role role = ctx.getBean("role", Role.class);
        System.out.println(role.getRoleName());
    }
}
