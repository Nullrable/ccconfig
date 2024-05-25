package io.cc.config.client.config;

import io.cc.config.client.repository.RepositoryMeta;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.Environment;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author nhsoft.lsd
 */
public class CcConfigPostProcessor implements
        BeanFactoryPostProcessor, PriorityOrdered, EnvironmentAware, ApplicationContextAware {

    private static final String CC_PROPERTY_RESOURCE = "CC_CONFIG_PROPERTY_RESOURCE";

    private Environment environment;

    private ApplicationContext applicationContext;

    @Override
    public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException {
        ConfigurableEnvironment ENV = (ConfigurableEnvironment) environment;

        RepositoryMeta configMeta = new RepositoryMeta();
        configMeta.setApp(ENV.getProperty("ccconfig.app", "application"));
        configMeta.setEnv(ENV.getProperty("ccconfig.env", "dev"));
        configMeta.setNs(ENV.getProperty("ccconfig.ns", "public"));
        configMeta.setServer(ENV.getProperty("ccconfig.server", "http://localhost:9000"));

        CcConfigService ccConfigService = CcConfigService.getDefault(applicationContext, configMeta);

        CcPropertySource ccPropertySource = new CcPropertySource(CC_PROPERTY_RESOURCE, ccConfigService);

        ENV.getPropertySources().addFirst(ccPropertySource);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void setEnvironment(final Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
