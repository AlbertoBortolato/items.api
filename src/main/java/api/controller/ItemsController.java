package api.controller;

import api.model.Items;
import api.service.ItemsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.*;

/**
 * Created by matthew on 28.04.16.
 */
@RestController
@RequestMapping("api")
public class ItemsController {

    @Inject
    ItemsService service;

    @RequestMapping(value = {"/api", ""},
      method = RequestMethod.POST,
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Items> addItems(@RequestBody Items.Item item) {
        Items.Item out = new Items.Item();
        out = service.push(item);

        if (out == null) {
            return new ResponseEntity<Items>(null, null, HttpStatus.BAD_REQUEST);
        } else {
            // service.save(item);
            return new ResponseEntity<Items>(service.items(), null, HttpStatus.OK);
        }
    }
    @RequestMapping(value = {"/api"},
      method = RequestMethod.DELETE,
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Items> removeItems(@RequestBody String id) {

        boolean b = service.delete(id);
        if(b) {
            return new ResponseEntity<Items>(null, null, HttpStatus.OK);
        }else {
            return new ResponseEntity<Items>(null, null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = {"/"},
      method = RequestMethod.PUT,
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Items> updateItems(@RequestBody String id, Items.Item new_item) {

        boolean b = service.update(new_item, id);
        if(b) {
            return new ResponseEntity<Items>(null, null, HttpStatus.OK);
        }else{
            return new ResponseEntity<Items>(null, null, HttpStatus.NOT_MODIFIED);
        }
    }

    @RequestMapping(value = {"/", ""},
      method = RequestMethod.GET,
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Items> getItems(@RequestParam Map<String, String> queryParamsMap) {

        return new ResponseEntity<Items>(service.items(), null, HttpStatus.OK);
    }

}
