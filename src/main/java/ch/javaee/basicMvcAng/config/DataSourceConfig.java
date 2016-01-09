package ch.javaee.basicMvcAng.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jndi.JndiObjectFactoryBean;

import javax.sql.DataSource;


/**
 * Created by marco on 09/01/16.
 */
@Configuration
@PropertySource(value = "classpath:/dev/database.properties", ignoreResourceNotFound = true)
public class DataSourceConfig {

    @Autowired
    Environment env;

    @Bean
    @Profile("dev")
    public DataSource testDataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(env.getProperty("dev.jdbc.url"));
        ds.setUsername(env.getProperty("dev.jdbc.username"));
        ds.setPassword(env.getProperty("dev.jdbc.password"));
        return ds;
    }

    @Bean
    @Profile("prod")
    public DataSource prodDataSource() {
        JndiObjectFactoryBean ds = new JndiObjectFactoryBean();
        ds.setLookupOnStartup(true);
        ds.setJndiName("jdbc/prodConnection");
        ds.setCache(true);

        return (DataSource) ds.getObject();
    }

}
