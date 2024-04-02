package com.didan.social.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class DatabaseConfig implements CommandLineRunner {
    private final DataSource dataSource;
    @Autowired
    public DatabaseConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) throws Exception {
        Connection connection = dataSource.getConnection();
        ClassPathResource resource = new ClassPathResource("db.sql");
        ScriptUtils.executeSqlScript(connection, resource);
        connection.close();
    }
}
