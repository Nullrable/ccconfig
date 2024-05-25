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

    public static String getVersionKey(ConfigMeta meta) {
        return meta.getApp() + "_" + meta.getEnv() + "_" + meta.getNs();
    }

    public static String getVersionKey(String app, String env, String ns) {
        return app + "_" + env + "_" + ns;
    }
}
