package io.cc.config.server.mapper;

import io.cc.config.server.model.ConfigMeta;
import io.cc.config.server.model.ConfigVersionMeta;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author nhsoft.lsd
 */
@Mapper
public interface ConfigVersionMapper {

    @Insert("insert into config_version (app, env, ns, version) values(#{app}, #{env}, #{ns}, #{version} )")
    int save(ConfigVersionMeta meta);

    @Update("update config_version set version = #{version} where app = #{app} and env = #{env} and ns = #{ns}" )
    int update(ConfigVersionMeta meta);

    @Select("select * from config_version where app = #{app} and env = #{env} and ns = #{ns}")
    ConfigVersionMeta read(String app, String env, String ns);

    @Select("select * from config_version")
    List<ConfigVersionMeta> lisAll();
}
