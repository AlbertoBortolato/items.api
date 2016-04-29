package api.model;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by matthew on 28.04.16.
 */
public class Items {
    Set<Item> items = new HashSet<>();                  //Il set non contiene doppioni a differenza della lista
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

        public Item(){
            id = UUID.randomUUID().toString();
        }       //UUID ==> ID univoco (pesano un pò di più)

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

        public Date getCreationDate() {
            return creationDate;
        }

        public void setCreationDate(Date creationDate) {
            this.creationDate = creationDate;
        }

        public Date getModifiedDate() {
            return modifiedDate;
        }

        public void setModifiedDate(Date modifiedDate) {
            this.modifiedDate = modifiedDate;
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
