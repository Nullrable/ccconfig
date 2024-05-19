package io.cc.config.client.repository;

import java.util.Map;

/**
 * @author nhsoft.lsd
 */
public class ChangeEvent {

    private Map<String, String> configs;

    public ChangeEvent(final Map<String, String> configs) {
        this.configs = configs;
    }

    public Map<String, String> getConfigs() {
        return configs;
    }
}
