package io.cc.config.server.service;

import io.cc.config.server.mapper.ConfigVersionMapper;
import io.cc.config.server.model.ConfigMeta;
import io.cc.config.server.mapper.ConfigMapper;
import io.cc.config.server.model.ConfigVersionMeta;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author nhsoft.lsd
 */
@Service
@Slf4j
public class ConfigServiceImpl implements ConfigService{

    @Resource
    private ConfigMapper configMapper;

    @Resource
    private ConfigVersionMapper configVersionMapper;

    private final Map<String, Long> VERSIONS = new HashMap<>();

    private Map<String, List<ConfigMeta>> configCache = new HashMap<>();

    private ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1);;

    @PostConstruct
    private void init() {
        executor.scheduleWithFixedDelay(this::refresh, 1, 5, TimeUnit.SECONDS);
    }

    private void refresh() {

        List<ConfigVersionMeta> configVersionMetas = configVersionMapper.lisAll();

        configVersionMetas.forEach(meta -> {

            String key = meta.getVersionKey();
            long newVersion = meta.getVersion();
            long oldVersion = VERSIONS.getOrDefault(key, 0L);
            if (newVersion > oldVersion) {
                log.info("refresh config version {} -> {}", key, newVersion);
                VERSIONS.put(key, newVersion);
                configCache.remove(key);
            }
        });
    }

    @Override
    public ConfigMeta saveOrUpdate(final ConfigMeta meta) {
        ConfigMeta persistent = configMapper.read(meta.getApp(), meta.getEnv(), meta.getNs(), meta.getPkey());
        if (persistent != null) {
            configMapper.update(meta);
        } else {
            configMapper.save(meta);
        }

        long version = System.currentTimeMillis();
        String key = ConfigMeta.getVersionKey(meta);
        VERSIONS.put(key, version);
        configCache.remove(key);

        ConfigVersionMeta versionPersistent = configVersionMapper.read(meta.getApp(), meta.getEnv(), meta.getNs());

        ConfigVersionMeta configVersionMeta = new ConfigVersionMeta();
        configVersionMeta.setApp(meta.getApp());
        configVersionMeta.setEnv(meta.getEnv());
        configVersionMeta.setNs(meta.getNs());
        configVersionMeta.setVersion(version);

        if (versionPersistent == null) {
            configVersionMapper.save(configVersionMeta);
        } else {
            configVersionMapper.update(configVersionMeta);
        }
        return meta;
    }

    @Override
    public List<ConfigMeta> listAll(final String app, final String env, final String ns) {

        String key = ConfigMeta.getVersionKey(app, env, ns);

        List<ConfigMeta> configMetas = configCache.get(key);

        if (configMetas != null && !configMetas.isEmpty()) {
            log.info("get config metas from cache {}", configMetas);
            return configMetas;
        }
        configMetas = configMapper.listAll(app, env, ns);
        configCache.put(key, configMetas);
        log.info("get config metas from database {}", configMetas);
        return  configMetas;
    }

    @Override
    public Long version(final String app, final String env, final String ns) {
        return VERSIONS.get(ConfigMeta.getVersionKey(app, env, ns));
    }
}
