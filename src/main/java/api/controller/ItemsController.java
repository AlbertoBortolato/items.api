package api.controller;

import api.model.Items;
import api.service.ItemsService;
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

    @Inject
    ItemsService service;

    @RequestMapping(value = {"/", ""},
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Items> getItems(@RequestParam Map<String, String> queryParamsMap) {

        return new ResponseEntity<Items>(service.items(), null, HttpStatus.OK);
    }

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
    @RequestMapping(value = {"/{id}"},
            method = RequestMethod.DELETE,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<?> deleteItem(@PathVariable("id") String id) {
        service.delete(id);

        ResponseEntity<?> response = new ResponseEntity<>(null, HttpStatus.OK);
        return response;
    }
    @RequestMapping(value = {"/{id}"},
            method = RequestMethod.PUT,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<?> updateItem(@RequestBody Items.Item item, @PathVariable("id") String id) {
        service.update(id, item);

        ResponseEntity<?> response = new ResponseEntity<>(null, HttpStatus.OK);
        return response;
    }



}
