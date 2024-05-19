package io.cc.config.client.config;

import io.cc.config.client.repository.ChangeEvent;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;

/**
 * @author nhsoft.lsd
 */
public class CcConfigServiceImpl implements CcConfigService{

    private Map<String, String> source;

    private ApplicationContext applicationContext;

    public CcConfigServiceImpl(final ApplicationContext applicationContext, final Map<String, String> source) {
        this.source = source;
        this.applicationContext = applicationContext;
    }

    @Override
    public String[] getPropertyNames() {
        return source.values().toArray(new String[0]);
    }

    @Override
    public String getProperty(final String name) {
        return source.get(name);
    }

    @Override
    public void onChanged(final ChangeEvent event) {

        Map<String, String> newConfigs = event.getConfigs();

        Set<String> changedkeys = new HashSet<>();

        newConfigs.forEach((key, newValue) -> {
            if (source.containsKey(key)) {
                String oldValue = source.get(key);
                if (!newValue.equals(oldValue)) {
                    source.put(key, newValue);
                    changedkeys.add(key);
                }
            } else {
                source.put(key, newValue);
                changedkeys.add(key);
            }
        });

        if (!changedkeys.isEmpty()) {
            applicationContext.publishEvent(new EnvironmentChangeEvent(changedkeys));
        }
    }
}
