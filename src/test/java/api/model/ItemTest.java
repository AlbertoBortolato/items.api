package api.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

//import static org.junit.Assert.*;

/**
 * Created by matthew on 03.05.16.
 */
public class ItemTest {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    Items.Item item;
    Items.Item item_test;

    public ItemTest() {

    }

    @Before
    public void setUp() throws Exception {
        item = new Items.Item();
        item_test = new Items.Item();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testEquals() throws Exception {
        boolean should_be_different = item.equals(item_test);
        logger.info("Should be different: " + should_be_different);
        assertThat(should_be_different).isFalse();
        item.setId(item_test.getId());
        boolean should_be_same = item.equals(item_test);
        logger.info("Should be same: " + should_be_same);
        assertThat(should_be_same).isTrue();


    }

    @Test
    public void testHashCode() throws Exception {
        boolean hashcode_should_be_different = (item_test.hashCode() == item.hashCode());
        item.setId(item_test.getId());
        boolean hashcode_should_be_same = (item_test.hashCode() == item.hashCode());
        String id = item.getId();
        boolean hashcode_should_be_same_1 = (id.hashCode() == item.hashCode());

    }

    @Test
    public void testToString() throws Exception {

    }

}