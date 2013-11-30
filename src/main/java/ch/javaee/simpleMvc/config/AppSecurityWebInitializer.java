package ch.javaee.simpleMvc.config;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * This class register the springSecurityFilterChain Filter for every URL
 */
@Order(2) // order in the filter chain
public class AppSecurityWebInitializer extends AbstractSecurityWebApplicationInitializer {
}
