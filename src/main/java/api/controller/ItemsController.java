package api.controller;

import api.model.Items;
import api.service.ItemsService;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import java.util.Map;

/**
 * Created by matthew on 28.04.16.
 */
@RestController
@RequestMapping("api")
public class ItemsController {
    Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    @Inject
    ItemsService service;


    @RequestMapping(value = {"/", ""},
      method = RequestMethod.POST,
      consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<?> createItem(UriComponentsBuilder builder, @RequestBody Items.Item item) {
        Items.Item out = service.add(item);
        if (out == null) {
            return new ResponseEntity<>(null, null, HttpStatus.BAD_REQUEST);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/api/{id}").buildAndExpand(out.getId()).toUri());
        ResponseEntity<Items.Item> response = new ResponseEntity<>(null, headers, HttpStatus.CREATED);
        return response;
    }


    @RequestMapping(value = {"/{id}"},
      method = RequestMethod.GET,
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<?> getItem(@PathVariable("id") String id) {
        Items.Item out = service.getItem(id);

        ResponseEntity<?> response = new ResponseEntity<>(out, null, HttpStatus.OK);
        return response;
    }

    @RequestMapping(value = {"/api", ""},
      method = RequestMethod.POST,
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Items> addItems(@RequestBody Items.Item item) {
        Items.Item out;
        out = service.push(item);

        if (out == null) {
            return new ResponseEntity<Items>(null, null, HttpStatus.BAD_REQUEST);
        } else {
            // service.save(item);
            return new ResponseEntity<Items>(service.items(), null, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{id}",
      method = RequestMethod.DELETE,
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Items> removeItems(@PathVariable("id") String id) {

        boolean b = service.delete(id);
        if (b) {
            return new ResponseEntity<Items>(null, null, HttpStatus.OK);

        } else {
            return new ResponseEntity<Items>(null, null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{id}",
      method = RequestMethod.PUT,
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Items> updateItems(@PathVariable("id") String id, @RequestBody Items.Item new_item) {

        boolean b = service.update(new_item, id);
        if (b) {
            log.info("", service.items().getItems());
            return new ResponseEntity<Items>(null, null, HttpStatus.OK);
        } else {
            return new ResponseEntity<Items>(null, null, HttpStatus.NOT_MODIFIED);
        }
    }


    @RequestMapping(value = {"/{id}"},
      method = RequestMethod.DELETE,
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<?> deleteItem(@PathVariable("id") String id) {
        service.delete(id);

        ResponseEntity<?> response = new ResponseEntity<>(null, HttpStatus.OK);
        return response;
    }

}
