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
    Logger logger = LoggerFactory.getLogger(this.getClass()); // stampa su console ma puo essere configurato anche per stampare su file e su email. Output può essere configurato in qualche modo
                                                              // si lavora su livelli : error stampa solo errori, info stampa errori e info, debug stampa debug error e info, trace stampa tutto.
    @Value("${api.items.use_authentication:true}")
    boolean useAuthentication;
    @Value("${api.items.user.admin.pass:admin-1}")
    String userAdminPass;
    @Value("${api.items.user.default:default-1}")
    String userDefaultPass;
    @Value("${api.items.user.admin:admin}") //i due utenti admin e dafaut
    String userAdmin;
    @Value("${api.items.user.default:default}")
    String userDefault;
    @Value("${api.items.user.admin.rolse:ADMIN}") //ruoli a cui appartiene utente admin . utente admin ruolo ADMIN
    List<String> userAdminRoles;
    @Value("${api.items.user.default.roles:USER}") //ruoli a cui appartiene utente default . utente admin ruolo USER
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
            // @formatter:off // TODO: 28/04/2016
            auth
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
            if (useAuthentication) { // configurazione sicurezza sugli end point .
                // @formatter:off
                http
                        .csrf() // server decide se i suoi metodi possono essere chiamati da origini diversi
                            .disable()
                            .authorizeRequests() // con autenticazione attive voglio che tutte le chiamate passano attraverso filtri di autorizzazione
                        .antMatchers(HttpMethod.GET, "/api") // con antMatchers impostiamo i ruoli
                            .hasAnyRole("ADMIN", "USER")
                        .antMatchers(HttpMethod.PUT, "/api")
                            .hasAnyRole("ADMIN", "USER")
                        .antMatchers(HttpMethod.POST, "/api")
                            .hasAnyRole("ADMIN", "USER")
                        .antMatchers(HttpMethod.DELETE, "/api")
                            .hasAnyRole("ADMIN")
                        .antMatchers("/**") // non è indicato il metodo , solo amministratore può vedere questi metodi(hasRole (admin))
                            .hasRole("ADMIN")
                        .and()
                            .sessionManagement() // una volta che ti loggi viene impostato un cookie sulla chiamata. tiene la sessione
                            .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                        .and()
                            .formLogin()
                        .and()
                            .httpBasic()
                        .and()
                            .logout()
                        .and()
                            .rememberMe() // ti permette spuntando la casella di tenere le credenziali nel browser
                ;
                // @formatter:on
            } else {
                http.csrf().disable();
            }
        }
    }
}
