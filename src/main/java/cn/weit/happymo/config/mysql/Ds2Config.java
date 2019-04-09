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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author weitong
 */
@Configuration
@MapperScan(basePackages = "cn.weit.happymo.dao.ds2", sqlSessionTemplateRef  = "dataSource2SqlSessionTemplate")
public class Ds2Config {

    @Value("${mybatis.mapperLocations}")
    private String mapperLocations;

    @Bean(name = "dataSourceData2")
    @ConfigurationProperties(prefix = "spring.datasource.mysql2")
    public DataSource dataSourceData2() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "dataSource2SqlSessionFactory")
    public SqlSessionFactory dataSource2SqlSessionFactory(@Qualifier("dataSourceData2") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations( new PathMatchingResourcePatternResolver().getResources(mapperLocations));
        return bean.getObject();
    }

    @Bean(name = "dataSource2TransactionManager")
    public DataSourceTransactionManager dataSource2TransactionManager(@Qualifier("dataSourceData2") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "dataSource2SqlSessionTemplate")
    public SqlSessionTemplate dataSource2SqlSessionTemplate(@Qualifier("dataSource2SqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
