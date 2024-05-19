package io.cc.config.client.repository;

import java.util.Map;

/**
 * @author nhsoft.lsd
 */
public interface CcRepository {

    static CcRepository getDefault(RepositoryMeta serverMeta) {

        CcRepository ccRepository = new CcRepositoryImpl(serverMeta);

        return ccRepository;
    }

    Map<String, String> getConfig();

    void addChangeListener(ConfigChangedListener listener);
}
