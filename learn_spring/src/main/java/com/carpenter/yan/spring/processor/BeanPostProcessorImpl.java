package com.carpenter.yan.spring.processor;

import com.carpenter.yan.spring.domain.Life;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component("beanPostProcessor")
public class BeanPostProcessorImpl implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("【"+ bean.getClass().getSimpleName()+"】"+beanName+"开始初始化");
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("【"+ bean.getClass().getSimpleName()+"】"+beanName+"结束初始化");
        if(beanName.equals("life")){
            Life life = new Life();
            return life;
        }

        return bean;
    }
}
