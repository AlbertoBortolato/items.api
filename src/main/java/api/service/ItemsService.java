package api.service;

import api.model.Items;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.el.stream.Optional;
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

    public Items.Item pullItem(Items.Item item){
            item.setCreationDate(new Date());
            items.getItems().add(item);
            return item;
    }

    public Items getName(String name ){
        Items response= new Items();
        response.setItems(this.items().getItems().stream()
          .filter(item -> item.getName() != null)
          .filter(item -> item.getName().toLowerCase().equals(name.toLowerCase()))
          .collect(Collectors.toSet()));
        return response;
    }

    public Items getId(String id ){
        Items response= new Items();
        response.setItems(this.items().getItems().stream()
          .filter(item -> item.getId() != null)
          .filter(item -> item.getId().equals(id))
          .collect(Collectors.toSet()));
        return response;
    }


    public  void setItem (Items.Item item1,String id){
        java.util.Optional<Items.Item> found = this.items().getItems().stream()
          .filter(item -> item.getId().equals(id))
          .findFirst();
        if(!found.equals(java.util.Optional.empty())){
            this.items.getItems().remove(found);
            item1.setCreationDate(found.get().getCreationDate());
            item1.setId(found.get().getId());
            item1.setModifiedDate(new Date());
            this.items.getItems().add(item1);
        }
    }

    public boolean deleteItem (String id){
        java.util.Optional<Items.Item> found = this.items().getItems().stream()
          .filter(item -> item.getId().equals(id))
          .findFirst();
        if(!found.equals(java.util.Optional.empty())){
            this.items.getItems().remove(found.get());
            return true;
        }
        else{
            return false;
        }
    }

    public void save(Items items) {
        //write filePath
    }

    public Items items() {
        return this.items;
    }
}
