package io.cc.config.client.repository;

import lombok.Data;

/**
 * @author nhsoft.lsd
 */
@Data
public class RepositoryMeta {

    private String app;

    private String env;

    private String ns;

    private String server;

    public String genKey(){
        return app + "-" + env + "-" + ns;
    }

    String findAllPath() {
        return server + "/configs?app=" + app + "&env=" + env + "&ns=" + ns;
    }

    String versionPath() {
        return server + "/version?app=" + app + "&env=" + env + "&ns=" + ns;
    }
}
