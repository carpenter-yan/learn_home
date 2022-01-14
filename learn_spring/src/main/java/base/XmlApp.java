package base;

import base.spring.domain.Role;
import base.spring.domain.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

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

        User user = ctx.getBean("user", User.class);
        System.out.println(user.getUserName());

        Map<String, String> config = ctx.getBean("config", Map.class);
        System.out.println(config.get("key"));
    }
}
