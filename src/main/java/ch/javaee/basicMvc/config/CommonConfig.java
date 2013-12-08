package ch.javaee.basicMvc.config;

import ch.javaee.basicMvc.bean.MailBean;
import ch.javaee.basicMvc.service.MyUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;

@Configuration
@PropertySource("classpath:${ENV:dev}/mail.properties")
public class CommonConfig {

    static final Logger logger = LoggerFactory.getLogger(CommonConfig.class);

    @Value("${mail.subject}")
    private String mailSubject;
    @Value("${mail.from}")
    private String mailFrom;
    @Value("${mail.website.name}")
    private String mailWebsiteName;
    @Value("${mail.host}")
    private String mailHost;
    @Value("${mail.smtp.host}")
    private String mailSmtpHost;
    @Value("${mail.smtp.username}")
    private String mailSmtpUsername;

    @Value("${mail.smtp.password}")
    private String mailSmtpPassword;

    @Value("${mail.smtp.port}")
    private String mailSmtpPort;
    @Value("${mail.smtp.auth}")
    private String mailSmtpAuth;
    @Value("${mail.smtp.starttls.enable}")
    private String mailSmtpStartTlsEnable;

    @Bean
    JavaMailSender javaMailSender() {
        logger.debug("Enter: javaMailSender");
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setPort(Integer.parseInt(mailSmtpPort));
        javaMailSender.setHost(mailSmtpHost);
        javaMailSender.setUsername(mailSmtpUsername);
        javaMailSender.setPassword(mailSmtpPassword);

        javaMailSender.getJavaMailProperties().setProperty("mail.smtp.auth", mailSmtpAuth);
        javaMailSender.getJavaMailProperties().setProperty("mail.smtp.starttls.enable", mailSmtpStartTlsEnable);

        logger.debug("Exit: javaMailSender");
        return javaMailSender;
    }

    @Bean
    VelocityEngineFactoryBean velocityEngine() {
        VelocityEngineFactoryBean velocityEngine = new VelocityEngineFactoryBean();
        velocityEngine.setResourceLoaderPath("classpath:/mail/templates");
        return velocityEngine;
    }

    @Bean
    MyUserDetailsService myUserDetailsService() {
        return new MyUserDetailsService();
    }

    @Bean
    MailBean mailBean() {
        MailBean mailBean = new MailBean();
        mailBean.setHost(mailHost);
        mailBean.setSubject(mailSubject);
        mailBean.setWebsiteName(mailWebsiteName);
        mailBean.setFrom(mailFrom);
        return mailBean;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
