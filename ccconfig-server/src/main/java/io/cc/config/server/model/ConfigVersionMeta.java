package io.cc.config.server.model;

import lombok.Data;

/**
 * @author nhsoft.lsd
 */
@Data
public class ConfigVersionMeta {

    private String app;

    private String env;

    private String ns;

    private long version;

    public String getVersionKey() {
       return this.getApp() + "_" + this.getEnv() + "_" + this.getNs();
    }
}
