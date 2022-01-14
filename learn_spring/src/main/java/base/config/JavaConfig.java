package base.config;

import base.spring.aspect.Aspect;
import base.spring.domain.Domain;
import base.spring.processor.Processor;
import base.spring.service.Service;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan(basePackageClasses = {Domain.class, Service.class, Processor.class, Aspect.class})
@EnableAspectJAutoProxy
@ImportResource({"classpath:spring.xml"})
@PropertySource(value={"classpath:config.properties"}, encoding="UTF-8")
public class JavaConfig {
    @Bean
    public Map<String ,String> config(){
        Map<String, String> config = new HashMap<>();
        config.put("key", "value");
        return config;
    }

}
