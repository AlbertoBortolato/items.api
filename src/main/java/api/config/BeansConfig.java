package api.config;

import api.model.Items;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import javax.inject.Inject;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;

/**
 * Created by matthew on 28.04.16.
 */
@Configuration
public class BeansConfig {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${api.items.file}")
    String file;

    @Inject
    ObjectMapper mapper;

    @Bean
    public ConcurrentTaskScheduler taskScheduler(){
        ConcurrentTaskScheduler s = new ConcurrentTaskScheduler();
        s.setConcurrentExecutor(Executors.newFixedThreadPool(4));
        return s;
    }

    @Bean
    public Items items() {
        Path filePath = Paths.get(file);
        if(Files.exists(filePath)) {
            String sItems = null;
            try (FileInputStream fis = new FileInputStream(filePath.toFile())) {
                sItems = IOUtils.toString(fis, "UTF-8");
            } catch (FileNotFoundException e) {
                logger.error(e.getLocalizedMessage(), e);
            } catch (IOException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
            if(!StringUtils.isBlank(sItems)){
                try {
                    Items out = mapper.readValue(sItems, Items.class);
                    return out;
                } catch (IOException e) {
                    logger.error(e.getLocalizedMessage(), e);
                }
            }
        }
        //read filePath
        return new Items();
    }


}
