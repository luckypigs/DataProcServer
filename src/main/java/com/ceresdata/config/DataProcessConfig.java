package com.ceresdata.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 从配置文件中读取项目启动文件路径和文件名
 */
@Component
@ConfigurationProperties(prefix = "myserver")
public class DataProcessConfig {
    private String initFile;

    public String getInitFile() {
        return initFile;
    }

    public void setInitFile(String initFile) {
        this.initFile = initFile;
    }
}
