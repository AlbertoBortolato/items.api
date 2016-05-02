package api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by matthew on 28.04.16.
 */
@SpringBootApplication              //Possiamo chiamare @Bean
public class Application {
    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
    }
}
