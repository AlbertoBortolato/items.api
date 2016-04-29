package api.service;

import api.model.Items;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by matthew on 28.04.16.
 */
@Named // per questo devi creare un istanza di questa classe e lo usiamo come name.
public class ItemsService {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    ObjectMapper mapper;

    @Inject
    Items items;  // è quello creato nel beansConfig

    @Value("${api.items.file}")
    String file;
    Path filePath;

    @PostConstruct
    void init() {
        filePath = Paths.get(file);
    }

    public synchronized void save() {
        String jsonToSave = this.toJson(this.items());
        if(!StringUtils.isBlank(jsonToSave)) {
            //write filePath
            try {
                if (!Files.exists(filePath)) {
                    Files.createFile(filePath);
                }
            } catch (IOException ex) {
                logger.error(ex.getLocalizedMessage(), ex);
            }
            if (Files.exists(filePath)) {
                try(FileOutputStream fos = new FileOutputStream(filePath.toFile())){
                    fos.write(jsonToSave.getBytes("UTF-8"));
                } catch (UnsupportedEncodingException ex) {
                    logger.error(ex.getLocalizedMessage(), ex);
                } catch (IOException ex) {
                    logger.error(ex.getLocalizedMessage(), ex);
                }
            }
        }

    }
    private String toJson(Object obj) {
        String serializedObject = null;
        try {
            serializedObject = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.debug(e.getLocalizedMessage());
        }
        return serializedObject;
    }
    public Items.Item add(Items.Item item){
        this.items.getItems().add(item);
        this.save();
        return item;
    }
    public void update(String id, Items.Item update){
        Items.Item previous = new Items.Item();
        previous.setId(id);
        this.update(previous, update);
    }
    public void update(Items.Item previous, Items.Item update){
        if(this.items.getItems().contains(previous) && !this.items.getItems().contains(update)) {
            this.items.getItems().remove(previous);
            this.items.getItems().add(update);
            this.save();
        }
    }
    public void delete(String id){
        Items.Item item = new Items.Item();
        item.setId(id);
        this.delete(item);
    }
    public void delete(Items.Item item){
        if(this.items.getItems().contains(item)) {
            this.items.getItems().remove(item);
            this.save();
        }
    }
    public Items items() {
        return this.items;
    }
}
