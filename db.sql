CREATE TABLE config
(
    app   varchar(64)  not null comment '应用ID',
    env    varchar(64)  not null comment '环境',
    ns    varchar(64)  not null comment '命名空间',
    pkey varchar(64) not null comment '配置key',
    pval varchar(255)    not null comment '配置值',
    primary key (app, env, ns, pkey)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;