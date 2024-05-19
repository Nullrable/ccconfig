package io.cc.config.client.repository;

/**
 * @author nhsoft.lsd
 */
public interface ConfigChangedListener {

    void onChanged(ChangeEvent event);
}
