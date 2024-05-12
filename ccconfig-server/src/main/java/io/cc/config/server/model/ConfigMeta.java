package io.cc.config.server.model;

import lombok.Data;

/**
 * @author nhsoft.lsd
 */
@Data
public class ConfigMeta {

    private String app;

    private String env;

    private String ns;

    private String pkey;

    private String pval;
}
