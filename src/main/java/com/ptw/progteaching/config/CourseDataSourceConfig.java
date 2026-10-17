package com.ptw.progteaching.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
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
        basePackages = "com.ptw.progteaching.repository.course",
        jdbcAggregateOperationsRef = "courseJdbcAggregateTemplate",
        transactionManagerRef = "courseTransactionManager"
)
public class CourseDataSourceConfig {

    @Bean
    @ConfigurationProperties("con.course")
    public DataSourceProperties courseDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("con.course.hikari")
    public HikariDataSource courseDataSource(@Qualifier("courseDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    public NamedParameterJdbcTemplate courseNamedParameterJdbcOperations(@Qualifier("courseDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public JdbcDialect courseDialect() {
        return JdbcPostgresDialect.INSTANCE;
    }

    @Bean
    public JdbcCustomConversions courseJdbcCustomConversions() {
        return new JdbcCustomConversions();
    }

    @Bean
    public JdbcMappingContext courseJdbcMappingContext(@Qualifier("courseJdbcCustomConversions") JdbcCustomConversions conversions) {
        JdbcMappingContext context = new JdbcMappingContext(DefaultNamingStrategy.INSTANCE);
        context.setSimpleTypeHolder(conversions.getSimpleTypeHolder());

        return context;
    }

    @Bean
    public DataAccessStrategy courseDataAccessStrategy(@Qualifier("courseNamedParameterJdbcOperations") NamedParameterJdbcTemplate jdbcTemplate,
                                                       @Qualifier("courseJdbcConverter") JdbcConverter converter,
                                                       @Qualifier("courseDialect") JdbcDialect dialect) {

        return new DataAccessStrategyFactory(
                converter,
                jdbcTemplate,
                dialect,
                new DefaultQueryMappingConfiguration()
        ).create();
    }

    @Bean
    public JdbcConverter courseJdbcConverter(@Qualifier("courseJdbcMappingContext") JdbcMappingContext mappingContext,
                                             @Qualifier("courseNamedParameterJdbcOperations") NamedParameterJdbcTemplate jdbcTemplate,
                                             @Lazy @Qualifier("courseDataAccessStrategy") RelationResolver relationResolver,
                                             @Qualifier("courseJdbcCustomConversions") JdbcCustomConversions conversions,
                                             @Qualifier("courseDialect") JdbcDialect dialect) {

        DefaultJdbcTypeFactory typeFactory = new DefaultJdbcTypeFactory(
                jdbcTemplate.getJdbcOperations(),
                dialect.getArraySupport());

        return new MappingJdbcConverter(mappingContext, relationResolver, conversions, typeFactory);
    }

    @Bean
    public JdbcAggregateTemplate courseJdbcAggregateTemplate(ApplicationContext applicationContext,
                                                             @Qualifier("courseJdbcMappingContext") JdbcMappingContext mappingContext,
                                                             @Qualifier("courseJdbcConverter") JdbcConverter converter,
                                                             @Qualifier("courseDataAccessStrategy") DataAccessStrategy dataAccessStrategy) {

        return new JdbcAggregateTemplate(applicationContext, mappingContext, converter, dataAccessStrategy);
    }

    @Bean
    public DataSourceTransactionManager courseTransactionManager(@Qualifier("courseDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
