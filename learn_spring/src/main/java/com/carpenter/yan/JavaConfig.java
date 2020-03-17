package com.carpenter.yan;

import com.carpenter.yan.spring.domain.Domain;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {Domain.class})
public class JavaConfig {
}
