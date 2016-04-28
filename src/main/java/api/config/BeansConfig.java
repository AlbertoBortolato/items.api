package api.config;

import api.model.Items;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by matthew on 28.04.16.
 */
@Configuration
public class BeansConfig {

    @Value("${api.items.file}")
    String file;

    @Bean
    public Items items() {
        Path filePath = Paths.get(file);

        //read filePath
        return new Items();
    }


}
