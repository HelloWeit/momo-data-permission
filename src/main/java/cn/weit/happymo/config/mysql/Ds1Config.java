package cn.weit.happymo.config.mysql;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author weitong
 */
@Configuration
@MapperScan(basePackages = "cn.weit.happymo.dao.ds1", sqlSessionTemplateRef  = "dataSourceDataSqlSessionTemplate1")
public class Ds1Config {

    @Value("${mybatis.mapperLocations}")
    private String mapperLocations;

    @Bean(name = "dataSourceData1")
    @ConfigurationProperties(prefix = "spring.datasource.mysql1")
    @Primary
    public DataSource dataSourceData1() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "dataSourceSqlSessionFactory1")
    @Primary
    public SqlSessionFactory dataSourceSqlSessionFactory1(@Qualifier("dataSourceData1") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations( new PathMatchingResourcePatternResolver().getResources(mapperLocations));
        return bean.getObject();
    }

    @Bean(name = "dataSourceTransactionManager1")
    @Primary
    public DataSourceTransactionManager dataSourceTransactionManager1(@Qualifier("dataSourceData1") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "dataSourceDataSqlSessionTemplate1")
    @Primary
    public SqlSessionTemplate dataSourceDataSqlSessionTemplate1(@Qualifier("dataSourceSqlSessionFactory1") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
