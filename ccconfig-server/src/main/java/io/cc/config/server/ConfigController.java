package io.cc.config.server;

import io.cc.config.server.model.ConfigMeta;
import io.cc.config.server.service.ConfigService;
import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nhsoft.lsd
 */
@RestController
public class ConfigController {

    @Resource
    private ConfigService configService;

    @PostMapping("/configs")
    public ConfigMeta save(@RequestBody ConfigMeta configMeta) {
        return configService.saveOrUpdate(configMeta);
    }

    @GetMapping("/configs")
    public List<ConfigMeta> listAll(@RequestParam String app,
                                 @RequestParam String env,
                                 @RequestParam String ns) {
        return configService.listAll(app, env, ns);
    }

    @GetMapping("/version")
    public Long version(@RequestParam String app,
                                 @RequestParam String env,
                                 @RequestParam String ns) {
        return configService.version(app, env, ns);
    }
}
