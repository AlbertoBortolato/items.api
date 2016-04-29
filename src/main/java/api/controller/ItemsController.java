package api.controller;

import api.model.Items;
import api.service.ItemsService;
import javafx.scene.input.InputMethodTextRun;
import org.omg.CORBA.Request;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Map;

/**
 * Created by matthew on 28.04.16.
 */
@RestController
@RequestMapping("api")
public class ItemsController {

    @Inject
    ItemsService service;

    @RequestMapping(value = {"/", ""},
      method = RequestMethod.GET,
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Items> getItems(@RequestParam Map<String, String> queryParamsMap) {

        return new ResponseEntity<Items>(service.items(), null, HttpStatus.OK);
    }


    //curl -X POST -H "Content-Type: application/json" -d @test.txt http://admin:admin-1@localhost:8080/api
    @RequestMapping(value = {"/", ""},
      method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Items> addItemPost(@RequestParam Map<String, String> queryParamsMap,
                                             @RequestBody Items.Item item) {
        if (!service.items().getItems().contains(item)) {
            service.items().getItems().add(item);
            return new ResponseEntity<Items>(service.items(), null, HttpStatus.OK);
        }
        return new ResponseEntity<Items>(service.items(), null, HttpStatus.CONFLICT);
    }

    //curl -X DELETE -H "Content-Type: application/json" http://admin:admin-1@localhost:8080/api/{id}
    @RequestMapping(value = {"/{id}", ""},
      method = RequestMethod.DELETE,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> deleteItem(@RequestParam Map<String, String> queryParamsMap,
                                        @PathVariable("id") String id) {
        /*for (Items.Item it : service.items().getItems()) {
            if (it.getName().equals(name)) {
                service.items().getItems().remove(it);
                return new ResponseEntity<Object>(true, null, HttpStatus.OK);
            }
        }*/
        Items.Item item = new Items.Item();
        item.setId(id);
        if (service.items().getItems().contains(item.getId())){
            service.items().getItems().remove(item);
            return new ResponseEntity<Object>(service.items(), null, HttpStatus.OK);
        }
        return new ResponseEntity<Object>(service.items(), null, HttpStatus.NO_CONTENT);
    }

    //curl -X PUT -H "Content-Type: application/json" -d @test.txt http://admin:admin-1@localhost:8080/api/{id}
    @RequestMapping(value = {"/{id}", ""},
      method = RequestMethod.PUT,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> updateItem(@RequestParam Map<String, String> queryParamsMap,
                                        @PathVariable("id") String id,
                                        @RequestBody Items.Item item) {

        /*for (Items.Item it : service.items().getItems()) {
            if (it.getName().equals(name)) {
                it.setName(item.getName());
                return new ResponseEntity<Object>(service.items(), null, HttpStatus.OK);
            }
        }*/
        Items.Item itemNW = new Items.Item();
        itemNW.setId(id);
        if(service.items().getItems().contains(itemNW)) {
            service.items().getItems().remove(itemNW);
            service.items().getItems().add(item);
            return new ResponseEntity<Object>(service.items(), null, HttpStatus.OK);
        }

        return new ResponseEntity<Object>(service.items(), null, HttpStatus.NOT_FOUND);
    }

}
