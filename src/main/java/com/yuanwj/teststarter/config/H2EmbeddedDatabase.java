package com.yuanwj.teststarter.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @description: h2数据库配置
 * @author: yuanwj
 * @date: 2021/01/03 17:42
 **/
@ConditionalOnClass(name = "org.h2.Driver")
@EnableConfigurationProperties(EmbeddedProperties.class)
public class H2EmbeddedDatabase {

    public static volatile EmbeddedDatabase embeddedDatabase;

    @Bean
    public DataSource etDatasource(EmbeddedProperties embeddedProperties) {
        return embeddedDataSource(embeddedProperties);
    }


    public DataSource embeddedDataSource(EmbeddedProperties embeddedProperties) {
        final EmbeddedDatabaseFactory embeddedDatabaseFactory = new EmbeddedDatabaseFactory();
        final EmbeddedConfiguration embeddedConfiguration = new EmbeddedConfiguration(embeddedProperties.getUrl(),
                embeddedProperties.getUserName(), embeddedProperties.getPassword());
        embeddedDatabaseFactory.setDatabaseConfigurer(embeddedConfiguration);
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        if (StrUtil.isNotBlank(embeddedProperties.getInitPath())) {
            populator.addScript(new DefaultResourceLoader().getResource(embeddedProperties.getInitPath()));
        }
        if (StrUtil.isNotBlank(embeddedProperties.getInitSql())) {
            populator.addScripts(new ByteArrayResource(embeddedProperties.getInitSql().getBytes()));
        }
        embeddedProperties.getScanPaths().stream().map(this::getSqlResource)
                .filter(Objects::nonNull).forEach(populator::addScript);
        embeddedDatabaseFactory.setDatabasePopulator(populator);


        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();

        dataSourceBuilder.driverClassName(embeddedProperties.getDriverClassName());
        dataSourceBuilder.url(embeddedProperties.getUrl());
        dataSourceBuilder.password(embeddedProperties.getPassword());
        dataSourceBuilder.username(embeddedProperties.getUserName());
        return dataSourceBuilder.build();
    }

    private Resource getSqlResource(String path) {
        final File file = new File(path);
        StringBuffer sql = new StringBuffer("");
        List<File> files = Arrays.stream(file.listFiles(((dir, name) -> name.endsWith(".sql")))).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(files)) {
            return null;
        }
        for (File sqlFile : files) {
            sql.append(FileUtil.readUtf8String(sqlFile));
        }
        return new ByteArrayResource(sql.toString().getBytes());
    }
}
