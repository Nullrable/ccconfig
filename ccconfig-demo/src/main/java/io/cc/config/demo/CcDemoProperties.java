package io.cc.config.demo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author nhsoft.lsd
 */
@Data
@ConfigurationProperties(prefix = "cc")
public class CcDemoProperties {

    private String a;

    private String b;
}
