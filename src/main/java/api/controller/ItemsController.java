package api.controller;

import api.model.Items;
import api.service.ItemsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

/**
 * Created by matthew on 28.04.16.
 */
@RestController
@RequestMapping("api")
public class ItemsController {

    @Inject
    ItemsService service;


    @RequestMapping(value = "/",
      method = RequestMethod.GET,
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Items> getItems(@RequestParam(value = "search", required = false) String search) {

        if(search == null){
            return new ResponseEntity<Items>(service.items(), null, HttpStatus.OK);
        }

        return new ResponseEntity<Items>(service.search(search), null, HttpStatus.OK);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Items> updateItems(@PathVariable("id") String id, @RequestBody Items.Item nextItem) {

        if (service.update(id, nextItem)) {
            return new ResponseEntity<Items>(service.items(), null, HttpStatus.OK);
        }
        return new ResponseEntity<Items>(service.items(), null, HttpStatus.BAD_REQUEST);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Items> deleteItems(@PathVariable("id") String id) {

        if (service.delete(id)) {
            return new ResponseEntity<Items>(service.items(), null, HttpStatus.OK);
        }
        return new ResponseEntity<Items>(service.items(), null, HttpStatus.BAD_REQUEST);
    }


    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<Items> createItems(@RequestBody Items.Item item) {

        service.create(item);

        return new ResponseEntity<Items>(service.items(), null, HttpStatus.OK);
    }
}
