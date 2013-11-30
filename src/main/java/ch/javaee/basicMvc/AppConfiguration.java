package ch.javaee.basicMvc;

import ch.javaee.basicMvc.config.DispatcherConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@Configuration
@EnableWebMvc
@EnableWebSecurity
@ComponentScan(basePackages = "ch.javaee.basicMvc")
@Import({ WebInitializer.class, DispatcherConfig.class})
public class AppConfiguration {

}
