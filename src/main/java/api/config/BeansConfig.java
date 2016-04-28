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
    String file;                  //questo all'avvio dovrebbe prendere valore scritto nella riga 4 in application properties

    @Bean  // una volta che lo carico viene messo dentro un raccoglitore di oggetti Spring, questi sono riutilizzabili. sono indicizzati per tipo e per nome.
    public Items items() {    // avremo un bean di tipo Items con il nome items
        Path filePath = Paths.get(file);

        //read filePath
        return new Items();
    }


}
