package ch.javaee.simpleMvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.extras.springsecurity3.dialect.SpringSecurityDialect;
import org.thymeleaf.spring3.SpringTemplateEngine;
import org.thymeleaf.spring3.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import java.util.HashSet;
import java.util.Set;

@Configuration
@PropertySource("classpath:thymeleaf.properties")
public class ThymeleafConfig {


    @Bean
    public TemplateResolver templateResolver(){
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
        templateResolver.setPrefix("/WEB-INF/pages/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");

        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine(){
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        SpringSecurityDialect springSecurityDialect = new SpringSecurityDialect();
        Set<IDialect> dialects = new HashSet<>();
        dialects.add(springSecurityDialect);
        templateEngine.setAdditionalDialects(dialects);
        return templateEngine;
    }

    @Bean
    public ViewResolver viewResolver(){
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver() ;
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setOrder(1);

        return viewResolver;
    }

    /**
     //Example for JSP
     @Bean
     public ViewResolver viewResolver() {
         InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
         viewResolver.setViewClass(JstlView.class);
         viewResolver.setPrefix("/WEB-INF/pages/");
         viewResolver.setSuffix(".jsp");

         return viewResolver;
     }
     **/
}
