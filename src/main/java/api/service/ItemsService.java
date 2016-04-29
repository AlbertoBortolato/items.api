package api.service;

import api.model.Items;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by matthew on 28.04.16.
 */
@Named                              //Diciamo al contenitore di creare un istanza come classe e usarlo come bean
public class ItemsService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

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

    public void addSingle(Items.Item item) {

        items.getItems().add(item);
    }

    public void addAll(List<Items.Item> item) {

        items.getItems().addAll(item);
    }

    public void deleteById(String id) {
        Items.Item item = new Items.Item();
        item.setId(id);

        this.deleteByObject(item);
    }

    public void deleteByObject(Items.Item obj) {

        if (items.getItems().contains(obj)) {
            items.getItems().remove(obj);
        } else {
            logger.info("Item not found: " + obj);
        }
    }

    public void updateObject(Items.Item updated, Items.Item toUpdate) {

        if (items.getItems().contains(toUpdate)) {
            this.items.getItems().remove(toUpdate);
            this.items.getItems().add(updated);
        } else {
            logger.info("Item not found: " + toUpdate);
        }
    }

    public Items items() {
        return this.items;
    }
}
