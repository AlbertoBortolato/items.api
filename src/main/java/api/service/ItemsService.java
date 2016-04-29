package api.service;

import api.model.Items;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

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
        if (!response.equals(Optional.empty())) {
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
        if (!response.equals(Optional.empty())) {
            this.items().getItems().remove(response.get());
            this.items.getItems().add(new_item);
            b = true;
        } else {
            b = false;
        }

        return b;
    }


    public void save(Items.Item itemToSave, Path p) {
        if(Files.exists(p)) {
            FileOutputStream fosOldWay = null;
            try{
                fosOldWay = new FileOutputStream(p.toFile());
                fosOldWay.write(itemToSave.getContent(), p);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }finally {
                IOUtils.closeQuietly(fosOldWay);
            }
            try(FileOutputStream fos = new FileOutputStream(p.toFile())) {

            } catch (Exception e) {
                e.printStackTrace();
            }
            ;
        }
    }

    public Items items() {
        return this.items;
    }
}
