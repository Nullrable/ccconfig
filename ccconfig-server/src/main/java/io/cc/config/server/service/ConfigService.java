package io.cc.config.server.service;

import io.cc.config.server.model.ConfigMeta;
import java.util.List;

/**
 * @author nhsoft.lsd
 */
public interface ConfigService {

    ConfigMeta saveOrUpdate(ConfigMeta meta);

    List<ConfigMeta> listAll(String app, String env, String ns);

    Long version(String app, String env, String ns);
}
