package com.gonwan.springcloud.zipkin;

import java.util.concurrent.Executor;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import zipkin.server.EnableZipkinServer;

@SpringBootApplication
@EnableZipkinServer
public class ZipkinApplication {

    /* Override definitions in ZipkinMySQLStorageConfiguration */
    @Bean
    public Executor mysqlExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("ZipkinMySQLStorage-");
        executor.initialize();
        return executor;
    }

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource mysqlDataSource() {
        return DataSourceBuilder.create().build();
    }

    public static void main(String[] args) {
        SpringApplication.run(ZipkinApplication.class, args);
    }

}
