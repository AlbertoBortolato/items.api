package api.service;

import api.model.Items;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by matthew on 28.04.16.
 */
@Named  //creare un'istanza singola della classe condivista tra tutte le classe
public class ItemsService {
    Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
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


    public Items.Item push(Items.Item item) {
        this.items.getItems().add(item);
        return item;
    }

    public boolean delete(String id) {
        boolean b = false;
        Optional<Items.Item> response = this.items().getItems().stream()
          .filter(item -> item.getId() != null)
          .filter(item -> item.getId().equals(id))
          .findFirst();
        if(!response.equals(Optional.empty())){
            this.items().getItems().remove(response.get());
            b = true;
        }

        return b;
    }

    public boolean update(Items.Item new_item, String id) {
        boolean b = false;
        Optional<Items.Item> response = this.items().getItems().stream()
          .filter(item -> item.getId() != null)
          .filter(item -> item.getId().equals(id))
          .findFirst();
        if(!response.equals(Optional.empty()))  {
            this.items().getItems().remove(response.get());
            this.items.getItems().add(new_item);
            b = true;
        }else{
            b = false;
        }

        return b;
    }


    public void save(Items.Item items) {
        //write filePath
    }

    public Items items() {
        return this.items;
    }
}
