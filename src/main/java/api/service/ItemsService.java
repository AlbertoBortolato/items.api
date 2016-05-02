package api.service;

import api.model.IdNotSpecifiedException;
import api.model.Items;
import api.model.NotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.stream.Collectors;

/**
 * Created by matthew on 28.04.16.
 */
@Named
public class ItemsService {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    ObjectMapper mapper;
    @Inject
    ConcurrentTaskScheduler taskScheduler;

    @Inject
    Items items;

    @Value("${api.items.file}")
    String file;
    Path filePath;
    Boolean fileLock = false;

    @PostConstruct
    void init() {
        filePath = Paths.get(file);
        final ItemsService service = this;
        taskScheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                service.save();
            }
        }, DateTime.now().toDate(), 15000 );
    }

    public synchronized void save() {
        final Items toSave = this.items();
        final ItemsService service = this;
        if(!service.fileLock) {
            service.fileLock = true;
            Callable<Items> save = new Callable<Items>() {
                @Override
                public Items call() throws Exception {
                    String jsonToSave = service.toJson(toSave);
                    if (!StringUtils.isBlank(jsonToSave)) {
                        //write filePath
                        try {
                            if (!Files.exists(filePath)) {
                                Files.createFile(filePath);
                            }
                        } catch (IOException ex) {
                            logger.error(ex.getLocalizedMessage(), ex);
                        }
                        if (Files.exists(filePath)) {
                            try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
                                fos.write(jsonToSave.getBytes("UTF-8"));
                            } catch (UnsupportedEncodingException ex) {
                                logger.error(ex.getLocalizedMessage(), ex);
                            } catch (IOException ex) {
                                logger.error(ex.getLocalizedMessage(), ex);
                            }
                        }
                    }
                    service.fileLock = false;
                    return service.items();
                }
            };
            taskScheduler.submit(save);
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

    public Items.Item add(Items.Item item) {
        this.items.getItems().add(item);
        Date d = DateTime.now().toDate();
        item.setModifiedDate(d);
        item.setCreationDate(d);
        this.save();
        return item;
    }

    public void update(String id, Items.Item update) {
        Items.Item previous = this.getItem(id);
        this.update(previous, update);
    }

    public void update(Items.Item previous, Items.Item update) {
        if (this.items.getItems().contains(previous)) {
            if (!previous.equals(update)) {
                //ID's are different, just set them to be equal
                update.setId(previous.getId());
            }
            this.items.getItems().remove(previous);
            this.items.getItems().add(update);
            update.setCreationDate(previous.getCreationDate());
            update.setModifiedDate(DateTime.now().toDate());
            this.save();
        }
    }

    public void delete(String id) {
        Items.Item item = new Items.Item();
        item.setId(id);
        this.delete(item);
    }

    public void delete(Items.Item item) {
        if (this.items.getItems().contains(item)) {
            this.items.getItems().remove(item);
            this.save();
        }
    }

    public Items items() {
        return this.items;
    }

    public Items.Item getItem(String id) {
        if (!StringUtils.isBlank(id)) {
            Optional<Items.Item> found = this.items().getItems().stream()
                    .filter(item -> item.getId() != null)
                    .filter(item -> item.getId().toLowerCase().equals(id.toLowerCase()))
                    .findFirst();
            if (!found.equals(Optional.empty())) {
                return found.get();
            }
            throw new NotFoundException("Item with 'id' == '" + id + "'");
        }
        throw new IdNotSpecifiedException("Missing parameter 'id'");
    }

    public List<Items.Item> findItems(Map<String, String> queryParams){
        List<Items.Item> retItems = this.items().getItems().stream()
                .filter(item -> {
                    String query = queryParams.get("description");
                    if (!StringUtils.isBlank(query) && !StringUtils.isBlank(item.getDescription())) {
                        return item.getDescription().toLowerCase().contains(query.toLowerCase());
                    }
                    return true;
                })
                .filter(item -> {
                    String query = queryParams.get("content");
                    if (!StringUtils.isBlank(query) && !StringUtils.isBlank(item.getContent())) {
                        return item.getContent().toLowerCase().contains(query.toLowerCase());
                    }
                    return true;
                })
                .filter(item -> {
                    String query = queryParams.get("name");
                    if (!StringUtils.isBlank(query) && !StringUtils.isBlank(item.getName())) {
                        return item.getName().toLowerCase().contains(query.toLowerCase());
                    }
                    return true;
                })
                .filter(item -> {
                    if (queryParams.containsKey("created_before")) {
                        DateTime dt = DateTime.parse(queryParams.get("created_before"));
                        DateTime compare = new DateTime(item.getCreationDate());
                        return compare.isBefore(dt);
                    }
                    return true;
                })
                .filter(item -> {
                    if (queryParams.containsKey("created_after")) {
                        DateTime dt = DateTime.parse(queryParams.get("created_after"));
                        DateTime compare = new DateTime(item.getCreationDate());
                        return compare.isAfter(dt);
                    }
                    return true;
                })
                .filter(item -> {
                    if (queryParams.containsKey("modified_before")) {
                        DateTime dt = DateTime.parse(queryParams.get("modified_before"));
                        DateTime compare = new DateTime(item.getModifiedDate());
                        return compare.isBefore(dt);
                    }
                    return true;
                })
                .filter(item -> {
                    if (queryParams.containsKey("modified_after")) {
                        DateTime dt = DateTime.parse(queryParams.get("modified_after"));
                        DateTime compare = new DateTime(item.getModifiedDate());
                        return compare.isAfter(dt);
                    }
                    return true;
                })
                .collect(Collectors.toList());
        return retItems;


    }
}
