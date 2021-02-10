package com.yuanwj.teststarter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @description:
 * @author: yuanwj
 * @date: 2021/01/03 17:43
 **/
@Data
@ConfigurationProperties(prefix = "embedded.enabled")
public class EmbeddedProperties {
    public final String url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MYSQL";

    public final String userName = "app1";

    public final String password = "";

    public final String driverClassName = "org.h2.Driver";

    public final String initPath = "";

    private final List<String> scanPaths = Stream.of("../db/mysql").collect(Collectors.toList());

    private final String initSql = "";

}
