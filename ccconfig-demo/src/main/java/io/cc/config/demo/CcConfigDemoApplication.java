package io.cc.config.demo;

import io.cc.config.client.annotation.EnableCcConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.properties.ConfigurationPropertiesRebinder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableCcConfig
@EnableConfigurationProperties(CcDemoProperties.class)
public class CcConfigDemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext =   SpringApplication.run(CcConfigDemoApplication.class, args);

        System.out.println(" &&&& ====> " + applicationContext.getBean(ConfigurationPropertiesRebinder.class));
    }

}
