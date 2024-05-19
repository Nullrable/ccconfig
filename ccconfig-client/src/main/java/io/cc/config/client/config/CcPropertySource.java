package io.cc.config.client.config;

import org.springframework.core.env.EnumerablePropertySource;

/**
 * @author nhsoft.lsd
 */
public class CcPropertySource extends EnumerablePropertySource<CcConfigService> {

    public CcPropertySource(final String name, final CcConfigService source) {
        super(name, source);
    }

    @Override
    public String[] getPropertyNames() {
        return source.getPropertyNames();
    }

    @Override
    public String getProperty(final String name) {
        return source.getProperty(name);
    }
}
