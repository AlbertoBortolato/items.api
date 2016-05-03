package api.service;

import api.Application;
import api.model.Items;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.*;

/**
 * Created by matthew on 03.05.16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(
        classes ={
                Application.class
        }
)
@DirtiesContext
public class ItemsServiceIT {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    ItemsService service;
    @Inject
    Items items;
    @Inject
    ObjectMapper mapper;
    @Value("${api.items.file}")
    String file;
    Path filePath;

    @Before
    public void setUp() throws Exception {
        for(int i = 1; i <= 10000; i++){
            Items.Item item = new Items.Item();
            item.setId(Integer.toString(i));
            item.setName(String.format("item-%s", i));
            item.setContent(RandomStringUtils.random(25, true, true));
            item.setDescription(RandomStringUtils.random(150, true, true));
            item.getValues().put("test", "test-todo-" + RandomStringUtils.random(15, true, true));
            for(int j = 0; j < 5; j++){
                item.getValues().put(RandomStringUtils.random(4, true, true), RandomStringUtils.random(25, true, true));
            }
            items.getItems().add(item);
            logger.debug("Added item: " + item.toString());
        }
    }

    @After
    public void tearDown() throws Exception {
        filePath = Paths.get(file);
        Files.deleteIfExists(filePath);
    }

    @Test
    public void add() throws Exception {
        Items.Item item = new Items.Item();
        item.setId("add-test");
        item.setName(String.format("item-%s", item.getId()));
        item.setContent(RandomStringUtils.random(25, true, true));
        item.setDescription(RandomStringUtils.random(150, true, true));
        item.getValues().put("test", "test-todo-" + RandomStringUtils.random(15, true, true));
        for(int j = 0; j < 5; j++){
            item.getValues().put(RandomStringUtils.random(4), RandomStringUtils.random(25, true, true));
        }
        assertThat(item).isNotIn(items.getItems());
        service.add(item);
        assertThat(item).isIn(items.getItems());
    }

    @Test
    public void update() throws Exception {

    }

    @Test
    public void delete() throws Exception {

    }

    @Test
    public void items() throws Exception {

    }

    @Test
    public void getItem() throws Exception {

    }

    @Test
    public void findItems() throws Exception {

    }

}