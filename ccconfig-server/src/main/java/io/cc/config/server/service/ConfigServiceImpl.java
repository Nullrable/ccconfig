package io.cc.config.server.service;

import io.cc.config.server.model.ConfigMeta;
import io.cc.config.server.mapper.ConfigMapper;
import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * @author nhsoft.lsd
 */
@Service
public class ConfigServiceImpl implements ConfigService{

    @Resource
    private ConfigMapper configMapper;

    private final Map<String, Long> VERSIONS = new HashMap<>();

    @Override
    public ConfigMeta saveOrUpdate(final ConfigMeta meta) {
        ConfigMeta persistent = configMapper.read(meta.getApp(), meta.getEnv(), meta.getNs(), meta.getPkey());
        if (persistent != null) {
            configMapper.update(meta);
        } else {
            configMapper.save(meta);
        }
        VERSIONS.put(meta.getApp() + "_" + meta.getEnv() + "_" + meta.getNs(), System.currentTimeMillis());
        return meta;
    }

    @Override
    public List<ConfigMeta> listAll(final String app, final String env, final String ns) {
        return configMapper.listAll(app, env, ns);
    }

    @Override
    public Long version(final String app, final String env, final String ns) {
        return VERSIONS.get(app + "_" + env + "_" + ns);
    }
}
