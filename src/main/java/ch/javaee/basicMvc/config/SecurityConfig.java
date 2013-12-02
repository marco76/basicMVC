package ch.javaee.basicMvc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@ComponentScan("ch.javaee.basicMvc.service")
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public UserDetailsService myUserDetailsService;

    /**
     * For our sample we use a simple user/password credential
     *
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(authenticationProvider());

    }

    /**
     * We don't check the credentials for the static content
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/assets/**");
    }

    /**
     *
     * @param http
     * @throws Exception
     */

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/users**", "/sessions/**").hasRole("ADMIN") //
                // everybody can access the main page ("/") and the signup page ("/signup")
                .antMatchers("/assets/**", "/", "/login", "/signup", "/public/**").permitAll().anyRequest().hasRole("USER") 

        ;
        FormLoginConfigurer formLoginConfigurer = http.formLogin();
        formLoginConfigurer.loginPage("/login").failureUrl("/login/failure").defaultSuccessUrl("/login/success").permitAll();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
         authenticationProvider.setPasswordEncoder(passwordEncoder());
         authenticationProvider.setSaltSource(reflectionSaltSource());
         authenticationProvider.setUserDetailsService(myUserDetailsService);

       return authenticationProvider;

      }
    @Bean
    public Md5PasswordEncoder passwordEncoder(){
        return new Md5PasswordEncoder();
    }
    @Bean
    public ReflectionSaltSource reflectionSaltSource(){
        ReflectionSaltSource reflectionSaltSource = new ReflectionSaltSource();
        reflectionSaltSource.setUserPropertyToUse("username");
        return reflectionSaltSource;
    }


}
