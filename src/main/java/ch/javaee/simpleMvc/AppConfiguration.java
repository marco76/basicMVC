package ch.javaee.simpleMvc;

import ch.javaee.simpleMvc.config.DispatcherConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "ch.javaee.simpleMvc")
@Import({ WebInitializer.class, DispatcherConfig.class})
public class AppConfiguration {


}
