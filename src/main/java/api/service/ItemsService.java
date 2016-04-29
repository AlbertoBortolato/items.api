package api.service;

import api.model.Items;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by matthew on 28.04.16.
 */
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

    public boolean pullItem(Items.Item item){
        if(!items.getItems().contains(item)){
            item.setCreationDate(new Date());
            items.getItems().add(item);
            return true;
        }
        else{
            return false;
        }
    }

    public Set<Items.Item> getName(String name ){
        Set<Items.Item> response = this.items().getItems().stream()
          .filter(item -> item.getName() != null)
          .filter(item -> item.getName().toLowerCase().equals(name.toLowerCase()))
          .collect(Collectors.toSet());
        return response;
    }

    public void save(Items items) {
        //write filePath
    }

    public Items items() {
        return this.items;
    }
}
