package api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by matthew on 28.04.16.
 */
public class Items {
    Set<Item> items = new HashSet<>();
    public Set<Item> getItems() {
        return items;
    }
    public void setItems(Set<Item> items) {
        this.items = items;
    }

    public static class Item{

        String id;
        String name;
        String description;
        String content;
        Date creationDate;
        Date modifiedDate;

        Map<String, Object> values = new HashMap<>();

        public Item(){
            this.id = UUID.randomUUID().toString();
            this.creationDate = DateTime.now().toDate();
            this.modifiedDate = this.creationDate;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @JsonIgnore
        public Date getModCreationDate() {
            return creationDate;
        }

        public String getCreationDate() {
            DateFormat df = new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy");
            return df.format(creationDate.getTime());
        }

        public void setCreationDate(Date creationDate) {
            this.creationDate = creationDate;
        }

        @JsonIgnore
        public Date getModModifiedDate() {
            return modifiedDate;
        }

        public String getModifiedDate() {
            DateFormat df = new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy");
            return df.format(modifiedDate.getTime());
        }

        public void setModifiedDate(Date modifiedDate) {
            this.modifiedDate = modifiedDate;
        }

        public Map<String, Object> getValues() {
            return values;
        }

        public void setValues(Map<String, Object> values) {
            this.values = values;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Item item = (Item) o;

            return id.equals(item.id);

        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }

        @Override
        public String toString() {
            return "Item{" +
                    "name='" + name + '\'' +
                    ", id='" + id + '\'' +
                    '}';
        }
    }
}
