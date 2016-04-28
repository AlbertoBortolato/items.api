package api.service;

import api.model.Items;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by matthew on 28.04.16.
 */  /*ItemsService è un singleton, cioè Il singleton rappresenta un tipo particolare di classe che garantisce che soltanto un’unica istanza della classe stessa possa essere creata all’interno di un programma.*/
@Named
public class ItemsService {

    @Inject
    ObjectMapper mapper;

    @Inject
    Items items;

    @Value("${api.items.file}")
    String file;
    Path filePath;

    @PostConstruct
    void init() {
        filePath = Paths.get(file);
    }

    public void save(Items items) {
        //write filePath
    }

    public Items items() {
        return this.items;
    }
}
