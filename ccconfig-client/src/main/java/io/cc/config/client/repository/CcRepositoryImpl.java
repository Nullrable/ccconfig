package io.cc.config.client.repository;

import com.alibaba.fastjson.TypeReference;
import io.cc.config.client.utils.HttpUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * @author nhsoft.lsd
 */
@Slf4j
public class CcRepositoryImpl implements CcRepository{

    private Map<String, Long> versions = new HashMap<>();

    private Map<String, Map<String, String>> configMap = new HashMap<>();

    private RepositoryMeta configMeta;

    private List<ConfigChangedListener> listeners = new ArrayList<>();

    private ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1);;

    public CcRepositoryImpl(RepositoryMeta configMeta) {
        this.configMeta = configMeta;
        executor.scheduleWithFixedDelay(this::heartbeat, 1, 5, TimeUnit.SECONDS);
    }

    @Override
    public Map<String, String> getConfig() {
        Map<String, String> configs = configMap.get(configMeta.genKey());
        if (configs != null && !configs.isEmpty()) {
            return configs;
        }
        return findAll();
    }

    @Override
    public void addChangeListener(final ConfigChangedListener listener) {
        listeners.add(listener);
    }

    private void heartbeat() {
        Long newVersion = Optional.ofNullable(HttpUtils.httpGet(configMeta.versionPath(), Long.class)).orElse(0L);

        String metaKey = configMeta.genKey();
        Long oldVersion = Optional.ofNullable(versions.get(metaKey)).orElse(0L);
        if (newVersion > oldVersion) {

            System.out.println("[CCCONFIG] current=" + newVersion+ ", old=" + oldVersion);
            System.out.println("[CCCONFIG] need update new configs.");

            Map<String, String> newConfigs = findAll();
            listeners.forEach(l -> l.onChanged(new ChangeEvent(newConfigs)));
            configMap.put(metaKey, newConfigs);
            versions.put(metaKey, newVersion);
        }
    }

    private Map<String, String> findAll() {
        List<ConfigMeta> configs = HttpUtils.httpGet(configMeta.findAllPath(), new TypeReference<List<ConfigMeta>>(){});
        if (configs == null || configs.isEmpty()) {
            return Map.of();
        }
        Map<String, String> resultMap = new HashMap<>();
        for (ConfigMeta config : configs) {
            resultMap.put(config.getPkey(), config.getPval());
        }
        return resultMap;
    }
}
