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

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

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
    public void testAdd() throws Exception {
        Items.Item item = new Items.Item();
        item.setId("add-test");
        item.setName(String.format("item-%s", item.getId()));
        item.setContent(RandomStringUtils.random(25, true, true));
        item.setDescription(RandomStringUtils.random(150, true, true));
        item.getValues().put("test", "test-todo-" + RandomStringUtils.random(15, true, true));
        for(int j = 0; j < 5; j++){
            item.getValues().put(RandomStringUtils.random(4), RandomStringUtils.random(250, true, true));
        }
        assertThat(item).isNotIn(items.getItems());
        service.add(item);
        assertThat(item).isIn(items.getItems());
    }

    @Test
    public void testUpdate() throws Exception {
        Items.Item preitem = new Items.Item();
        Items.Item postitem = new Items.Item();
        preitem.setName("pre");
        postitem.setName("post");
        service.add(preitem);
        service.update(preitem,postitem);
        assertThat(items.getItems().contains(postitem)).isTrue();
        Optional<Items.Item> found = items.getItems().stream()
          .filter(item -> item.getName()==preitem.getName())
          .findFirst();
        assertThat(found.equals(Optional.empty())).isTrue();
        assertThat(service.getItem(preitem.getId()).getModifiedDate()!=preitem.getModifiedDate()).isTrue();
        assertThat(service.getItem(preitem.getId()).getName().contains(postitem.getName())).isTrue();
    }

    @Test
    public void testDelete() throws Exception {
        Items.Item item = new Items.Item();
        String name = RandomStringUtils.random(1500, true, true);
        item.setName(name);
        service.add(item);
        assertThat(items.getItems().contains(item)).isTrue();
        service.delete(item);
        assertThat(items.getItems().contains(item)).isFalse();
    }

    @Test
    public void testItems() throws Exception {
        assertThat(service.items).isEqualTo(items);
        Items anotheritems = new Items();
        assertThat(service.items).isNotEqualTo(anotheritems);
    }

    @Test
    public void testGetItem() throws Exception {
        String name = RandomStringUtils.random(1500, true, true);
        String newname = RandomStringUtils.random(1500, true, true);
        Items.Item item1= new Items.Item();
        item1.setName(name);
        service.add(item1);
        service.getItem(item1.getId()).setName(newname);
        Optional<Items.Item> found = items.getItems().stream()
          .filter(item -> item.getName()==newname)
          .findFirst();
        assertThat(!found.equals(Optional.empty())).isTrue();
    }

    @Test
    public void testFindItems() throws Exception {
        String name = RandomStringUtils.random(150, true, true);
        Items.Item item1 = new Items.Item();
        item1.setName(name);
        Map<String,String> map = new HashMap<>();
        map.put("name",name);
        service.add(item1);
        assertThat(service.findItems(map)).isNotEmpty();
        String impossible = RandomStringUtils.random(1500, true, true);
        map.replace("name",impossible);
        assertThat(service.findItems(map)).isEmpty();
    }

}
