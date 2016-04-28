package api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by matthew on 28.04.16.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${api.items.use_authentication:true}")
    boolean useAuthentication;
    @Value("${api.items.user.admin.pass:admin-1}")
    String userAdminPass;
    @Value("${api.items.user.default.pass:default-1}")
    String userDefaultPass;
    @Value("${api.items.user.admin:admin}")
    String userAdmin;
    @Value("${api.items.user.default:default}")
    String userDefault;
    @Value("${api.items.user.admin.roles:ADMIN}")
    List<String> userAdminRoles;
    @Value("${api.items.user.default.roles:USER}")
    List<String> userDefaultRoles;


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Inject
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        if (useAuthentication) {
            List<String> adminRoles = userAdminRoles;
            List<String> userRoles = userDefaultRoles;
            // @formatter:off
            auth//// TODO: 28/04/2016 view->tool windows->TODO
              .inMemoryAuthentication()
              .passwordEncoder(bCryptPasswordEncoder())
              .withUser(userAdmin).password(bCryptPasswordEncoder().encode(userAdminPass)).roles(adminRoles.toArray(new String[]{})).and()
              .withUser(userDefault).password(bCryptPasswordEncoder().encode(userDefaultPass)).roles(userRoles.toArray(new String[]{}));
            // @formatter:on
        }
    }

    @Configuration
    public static class ApplicationSecurity extends WebSecurityConfigurerAdapter {

        @Value("${api.items.use_authentication:true}")
        boolean useAuthentication;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            if (useAuthentication) {
                // @formatter:off
                http
                        .csrf()
                            .disable()
                        .authorizeRequests()
                            .antMatchers(HttpMethod.GET, "/api")
                                .hasAnyRole("ADMIN", "USER")
                            .antMatchers(HttpMethod.PUT, "/api")
                                .hasAnyRole("ADMIN", "USER")
                            .antMatchers(HttpMethod.POST, "/api")
                                .hasAnyRole("ADMIN", "USER")
                            .antMatchers(HttpMethod.DELETE, "/api")
                                .hasAnyRole("ADMIN")
                            .antMatchers("/**")
                                .hasRole("ADMIN")
                        .and()
                            .sessionManagement()
                                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                        .and()
                            .formLogin()
                        .and()
                            .httpBasic()
                        .and()
                            .logout()
                        .and()
                            .rememberMe()
                ;
                // @formatter:on
            } else {
                http
                        .csrf()
                            .disable();
            }
        }
    }
}
