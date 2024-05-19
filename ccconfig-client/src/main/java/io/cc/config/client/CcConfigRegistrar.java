package io.cc.config.client;

import io.cc.config.client.config.CcConfigPostProcessor;
import io.cc.config.client.value.SpringValueProcessor;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author nhsoft.lsd
 */
public class CcConfigRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(final AnnotationMetadata importingClassMetadata, final BeanDefinitionRegistry registry) {
        register(registry, CcConfigPostProcessor.class);
        register(registry, SpringValueProcessor.class);
    }

    private void register(final BeanDefinitionRegistry registry, final Class<?> aClass) {
        if (registry.containsBeanDefinition(aClass.getName())) {
            return;
        }

        // 创建并注册Bean定义
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(aClass);

        // 注册Bean定义
        registry.registerBeanDefinition(aClass.getName(), beanDefinition);

        System.out.println("register " + aClass.getName());
    }
}
