package com.mircroservice.unir.orders.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.Connection;

@Configuration
public class DatabaseConfig {

    @Bean
    CommandLineRunner initDatabase(DataSource dataSource) {
        return args -> {
            try (Connection connection = dataSource.getConnection()) {
                // Ejecutar schema.sql
                ScriptUtils.executeSqlScript(
                        connection,
                        new ClassPathResource("database/schema.sql")
                );

                // Ejecutar data.sql
                ScriptUtils.executeSqlScript(
                        connection,
                        new ClassPathResource("database/data.sql")
                );
            } catch (Exception e) {
                // La BD ya existe o no es necesario crear
                System.out.println("Database already initialized or error: " + e.getMessage());
            }
        };
    }
}

