package com.yuanwj.teststarter.config;


/**
 * @description:
 * @author: yuanwj
 * @date: 2021/01/26 16:18
 **/
public class SqlFormat {

    public String format(String sql) {
        return sql.replaceAll("`", "");
    }
}
