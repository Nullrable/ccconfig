package io.cc.config.client.config;

import io.cc.config.client.repository.CcRepository;
import io.cc.config.client.repository.ConfigChangedListener;
import io.cc.config.client.repository.RepositoryMeta;
import java.util.Map;
import org.springframework.context.ApplicationContext;

/**
 * @author nhsoft.lsd
 */
public interface CcConfigService extends ConfigChangedListener {

    static CcConfigService getDefault(ApplicationContext applicationContext, RepositoryMeta serverMeta) {

        CcRepository ccRepository = CcRepository.getDefault(serverMeta);

        Map<String, String> source = ccRepository.getConfig();

        CcConfigService ccConfigService = new CcConfigServiceImpl(applicationContext, source);

        ccRepository.addChangeListener(ccConfigService);

        return ccConfigService;

    }

    String[] getPropertyNames();

    String getProperty(String name);
}
