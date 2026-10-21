package com.ptw.progteaching.common.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.jdbc.core.convert.*;
import org.springframework.data.jdbc.core.dialect.JdbcDialect;
import org.springframework.data.jdbc.core.dialect.JdbcPostgresDialect;
import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.repository.config.DefaultQueryMappingConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.relational.core.mapping.DefaultNamingStrategy;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJdbcRepositories(
        basePackages = "com.ptw.progteaching.common.repository.user",
        jdbcAggregateOperationsRef = "userJdbcAggregateTemplate",
        transactionManagerRef = "userTransactionManager"
)
public class UserDataSourceConfig {

    @Bean
    @ConfigurationProperties("con.user")
    public DataSourceProperties userDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean
    @ConfigurationProperties("con.user.hikari")
    public HikariDataSource userDataSource(@Qualifier("userDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Primary
    @Bean
    public NamedParameterJdbcTemplate userNamedParameterJdbcOperations(@Qualifier("userDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Primary
    @Bean
    public JdbcDialect userDialect() {
        return JdbcPostgresDialect.INSTANCE;
    }

    @Primary
    @Bean
    public JdbcCustomConversions userJdbcCustomConversions() {
        return new JdbcCustomConversions();
    }

    @Primary
    @Bean
    public JdbcMappingContext userJdbcMappingContext(@Qualifier("userJdbcCustomConversions") JdbcCustomConversions conversions) {
        JdbcMappingContext context = new JdbcMappingContext(DefaultNamingStrategy.INSTANCE);
        context.setSimpleTypeHolder(conversions.getSimpleTypeHolder());

        return context;
    }

    @Primary
    @Bean
    public DataAccessStrategy userDataAccessStrategy(@Qualifier("userNamedParameterJdbcOperations") NamedParameterJdbcTemplate jdbcTemplate,
                                                     @Qualifier("userJdbcConverter") JdbcConverter converter,
                                                     @Qualifier("userDialect") JdbcDialect dialect) {

        return new DataAccessStrategyFactory(
                converter,
                jdbcTemplate,
                dialect,
                new DefaultQueryMappingConfiguration()
        ).create();
    }

    @Primary
    @Bean
    public JdbcConverter userJdbcConverter(@Qualifier("userJdbcMappingContext") JdbcMappingContext mappingContext,
                                           @Qualifier("userNamedParameterJdbcOperations") NamedParameterJdbcTemplate jdbcTemplate,
                                           @Lazy @Qualifier("userDataAccessStrategy") RelationResolver relationResolver,
                                           @Qualifier("userJdbcCustomConversions") JdbcCustomConversions conversions,
                                           @Qualifier("userDialect") JdbcDialect dialect) {

        DefaultJdbcTypeFactory typeFactory = new DefaultJdbcTypeFactory(
                jdbcTemplate.getJdbcOperations(),
                dialect.getArraySupport());

        return new MappingJdbcConverter(mappingContext, relationResolver, conversions, typeFactory);
    }

    @Primary
    @Bean
    public JdbcAggregateTemplate userJdbcAggregateTemplate(ApplicationContext applicationContext,
                                                           @Qualifier("userJdbcMappingContext") JdbcMappingContext mappingContext,
                                                           @Qualifier("userJdbcConverter") JdbcConverter converter,
                                                           @Qualifier("userDataAccessStrategy") DataAccessStrategy dataAccessStrategy) {

        return new JdbcAggregateTemplate(applicationContext, mappingContext, converter, dataAccessStrategy);
    }


    @Primary
    @Bean
    public DataSourceTransactionManager userTransactionManager(@Qualifier("userDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
