package com.yuanwj.teststarter.config;

import cn.hutool.core.util.ClassUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.embedded.ConnectionProperties;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseConfigurer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @description:
 * @author: yuanwj
 * @date: 2021/01/04 13:47
 **/
@Slf4j
public class EmbeddedConfiguration implements EmbeddedDatabaseConfigurer {

    private final String url;

    private final String username;

    private final String password;

    public EmbeddedConfiguration(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }


    @Override
    public void configureConnectionProperties(ConnectionProperties connectionProperties, String database) {
        connectionProperties.setDriverClass(ClassUtil.loadClass("org.h2.Driver"));
        connectionProperties.setUrl(url);
        connectionProperties.setUsername(username);
        connectionProperties.setPassword(password);
    }

    @Override
    public void shutdown(DataSource dataSource, String s) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            if (connection != null) {
                Statement statement = connection.createStatement();
                statement.execute("SHUTDOWN");
            }

        } catch (SQLException throwables) {
            log.warn("数据库关闭异常");
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    log.warn("数据库关闭异常");
                }

            }
        }
    }
}
