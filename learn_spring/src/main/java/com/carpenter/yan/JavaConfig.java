package com.carpenter.yan;

import com.carpenter.yan.spring.domain.Domain;
import com.carpenter.yan.spring.service.Service;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {Domain.class, Service.class})
public class JavaConfig {
}
