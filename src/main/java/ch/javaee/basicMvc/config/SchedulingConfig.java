package ch.javaee.basicMvc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class SchedulingConfig implements AsyncConfigurer {
    static final Logger logger = LoggerFactory.getLogger(SchedulingConfig.class);

    @Override
    public Executor getAsyncExecutor() {
        logger.debug("Enter: getAsynchExecutor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(7);
        executor.setMaxPoolSize(42);
        executor.setQueueCapacity(11);
        executor.setThreadNamePrefix("AsyncExecutor-");
        executor.initialize();
        logger.debug("Exit: getAsynchExecutor");
        return executor;
    }

}
