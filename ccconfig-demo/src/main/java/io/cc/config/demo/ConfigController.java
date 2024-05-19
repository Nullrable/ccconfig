package io.cc.config.demo;

import jakarta.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nhsoft.lsd
 */
@RestController
public class ConfigController {

    @Resource
    private CcDemoProperties ccDemoProperties;

    @Value("${cc.d}")
    private String d;

    @GetMapping("/properties")
    public List<String> properties() {

        return Arrays.asList(ccDemoProperties.getA(), ccDemoProperties.getB(), d);
    }
}
