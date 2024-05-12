package io.cc.config.server.mapper;

import io.cc.config.server.model.ConfigMeta;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author nhsoft.lsd
 */
@Mapper
public interface ConfigMapper {

    @Insert("insert into config (app, env, ns, pkey, pval) values(#{app}, #{env}, #{ns}, #{pkey}, #{pval} )")
    int save(ConfigMeta meta);

    @Update("update config set pval = #{pval} where app = #{app} and env = #{env} and ns = #{ns} and pkey = #{pkey}" )
    int update(ConfigMeta meta);

    @Select("select * from config where app = #{app} and env = #{env} and ns = #{ns}")
    List<ConfigMeta> listAll(String app, String env, String ns);

    @Select("select * from config where app = #{app} and env = #{env} and ns = #{ns} and pkey = #{pkey}")
    ConfigMeta read(String app, String env, String ns, String pkey);
}
