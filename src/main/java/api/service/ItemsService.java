package api.service;

import api.model.Items;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

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

    public void save(Items items) {
        //write filePath
    }

    public void create(Items.Item item) {
        this.items().getItems().add(item);
    }

    public Items search(String search) {
        Items tempItems = new Items();
        List<Items.Item> tempList = new LinkedList<>(this.items().getItems());

        for(int i=0; i<tempList.size(); i++) {
            if(tempList.get(i).getName().contains(search)) {
                tempItems.getItems().add(tempList.get(i));
            }
        }
        return tempItems;
    }

    public boolean update(String id, Items.Item nextItem) {
        Items.Item prevItem = new Items.Item();
        prevItem.setId(id);
        if (this.items().getItems().contains(prevItem) && !this.items().getItems().contains(nextItem)) {
            return update(prevItem, nextItem);
        }
        return false;
    }

    public boolean update(Items.Item prevItem, Items.Item nextItem) {
        if (this.items().getItems().contains(prevItem) && !this.items().getItems().contains(nextItem)) {
            this.items().getItems().remove(prevItem);
            this.items().getItems().add(nextItem);
            return true;
        }
        return false;
    }

    public boolean delete(String id) {
        Items.Item temp = new Items.Item();
        temp.setId(id);
        if (this.items().getItems().contains(temp)) {
            return delete(temp);
        }
        return false;
    }

    public boolean delete(Items.Item item) {
        if (this.items().getItems().contains(item)) {
            this.items().getItems().remove(item);
            return true;
        }
        return false;
    }

    public Items items() {
        return this.items;
    }
}
