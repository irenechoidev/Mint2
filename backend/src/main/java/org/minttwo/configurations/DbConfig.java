package org.minttwo.configurations;

import org.hibernate.SessionFactory;
import org.minttwo.dataclients.AccountTransactionClient;
import org.minttwo.dataclients.Db;
import org.minttwo.dataclients.AccountClient;
import org.minttwo.dataclients.UserClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class DbConfig {
    @Value("${db.driver}")
    private String dbDriver;

    @Value("${db.url}")
    private String dbUrl;

    @Value("${db.username}")
    private String dbUsername;

    @Value("${db.password}")
    private String dbPassword;

    @Value("${db.packages_to_scan}")
    private String packagesToScan;

    @Bean
    public AccountClient accountClient() {
        return new AccountClient(db());
    }

    @Bean
    public UserClient userClient() {
        return new UserClient(db());
    }

    @Bean
    public AccountTransactionClient accountTransactionClient() {
        return new AccountTransactionClient(db());
    }

    @Bean
    public Db db() {
        return new Db(sessionFactory());
    }

    @Bean
    public SessionFactory sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(packagesToScan());
        sessionFactory.setHibernateProperties(hibernateProperties());

        try {
            sessionFactory.afterPropertiesSet();
        } catch (IOException e) {
            throw new RuntimeException("Failed to configure bean sessionFactory", e);
        }

        return sessionFactory.getObject();
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(dbDriver);
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }

    @Bean
    public Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.put("hibernate.show_sql", true);
        return hibernateProperties;
    }

    @Bean
    public String packagesToScan() {
        return packagesToScan;
    }
}

