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
 */ /*Questo è un file di configurazione*/
@Configuration
@EnableWebSecurity /*Avvia un utente di default e crea una password al volo. Posso aggiungerlo dove voglio, facendo si che l'applicazione diventi sicura. A questo @EnableSecurity segue la definizione di una classe*/
public class SecurityConfig {
    Logger logger = LoggerFactory.getLogger(this.getClass());/*Ci stampa su console come systm.out.println ma stampa anche su file e web, oltre che su console. Inltre l'output può essere configurato e eperemtte di lavorare sui livelli, soprattuttto se voglio tanti detttagli. I lvelli sono info, error, trace, e debug e quello di default è info. Info non da peso all'operazione, come error, con il vantaggio di nono perder cicli se facciamo cose importnti. Con logger posso filtrare anche per package. In tutte le classi di servizio è OBBLIGATORIO mettere questa riga*/

    @Value("${api.items.use_authentication:true}") /*@Value indica un valore di default, ma è meno rilevante di quanto sta nell'application.properties. Il value api.items.use_authentication=true di aplplication.properties è rilevante.*/
    boolean useAuthentication;
    @Value("${api.items.user.admin.pass:admin-1}")
    String userAdminPass;
    @Value("${api.items.user.default.pass:default-1}")
    String userDefaultPass;
    @Value("${api.items.user.admin:admin}")/*amministratore*/
    String userAdmin;
    @Value("${api.items.user.default:default}")/*utente visitatore*/
    String userDefault;
    @Value("${api.items.user.admin.roles:ADMIN}")/*Con @Value posso instanziare una lista di valroi; qui preciso il ruolo dell'utente admin che è ADMIN*/
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
            // @formatter:off  Questo commento dice alla ide di non formattare il testo quando schiacci ctrl+alt+l. Impostiamo i bin di binEncoder
            auth /*Questo modi di programmare si chiama fluent api, soprattutto per configurazione di cose si usa il fluent api che restituisce un oggetto apparentabile a se stesso. Lodsh usa la logica di chain ed è concatenabile.*/
                    .inMemoryAuthentication()
                    .passwordEncoder(bCryptPasswordEncoder())/*Salvo la password dell'utente già criptata*/
                    .withUser(userAdmin).password(bCryptPasswordEncoder().encode(userAdminPass)).roles(adminRoles.toArray(new String[]{})).and()/*Indichiamo la sua password blindata.  Da questo pto in poi il testo ha una chiave criptata. Nei bitcryptencoder le password non sono mai in vista, vengono memorizzate con caratteri criptatik*/
                    .withUser(userDefault).password(bCryptPasswordEncoder().encode(userDefaultPass)).roles(userRoles.toArray(new String[]{}));
            // @formatter:on.
        }//// TODO: 28/04/2016
    }

    @Configuration
    public static class ApplicationSecurity extends WebSecurityConfigurerAdapter {

        @Value("${api.items.use_authentication:true}")
        boolean useAuthentication;

        @Override
        protected void configure(HttpSecurity http) throws Exception {/*Qui configuriamo gli endpoint*/
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
                http.csrf().disable();
            }
        }
    }
}
