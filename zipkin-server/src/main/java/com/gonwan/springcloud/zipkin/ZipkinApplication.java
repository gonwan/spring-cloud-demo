package com.gonwan.springcloud.zipkin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zipkin.server.EnableZipkinServer;
import zipkin2.server.internal.mysql.ZipkinMySQLStorageConfiguration;

import javax.sql.DataSource;

@Configuration
@AutoConfigureBefore(ZipkinMySQLStorageConfiguration.class)
class ZipkinConfiguration {

    /* Override definitions in ZipkinMySQLStorageConfiguration */
    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSource mysqlDataSource() {
        return DataSourceBuilder.create().build();
    }

}

@EnableZipkinServer
@SpringBootApplication
public class ZipkinApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZipkinApplication.class, args);
    }

}
