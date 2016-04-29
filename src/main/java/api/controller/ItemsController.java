package api.controller;

import api.model.Items;
import api.service.ItemsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Created by matthew on 28.04.16.
 */
@RestController
@RequestMapping("api")
public class ItemsController {

    @Inject
    ItemsService service;

    @RequestMapping(value = {"/", ""}, method = RequestMethod.POST,
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
      consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Items.Item> create(@RequestBody Items.Item item1){
            return new ResponseEntity<Items.Item>(service.pullItem(item1), null, HttpStatus.CREATED);
    }

    @RequestMapping(value = {"/", ""},
      method = RequestMethod.GET,
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Items> getNome(@RequestParam(value = "query",required = false) String query){
        if (query != null) {
            System.out.print("ok");
            return new ResponseEntity<Items>(service.getName(query), null, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<Items>(service.items(), null, HttpStatus.OK);
        }

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE,
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Items> delete(@PathVariable("id") String id){
        Items deletato= service.getId(id);
        if(service.deleteItem(id)){
            return  new ResponseEntity<Items>(deletato, null, HttpStatus.OK);
        }
        else{
            return  new ResponseEntity<Items>(null,null,HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT,
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Items> update(@PathVariable("id") String id,@RequestBody Items.Item item1){
        Items modificato= service.getId(id);
        if(service.setItem(item1, id)){
            return  new ResponseEntity<Items>(modificato, null, HttpStatus.OK);
        }
        else{
            return  new ResponseEntity<Items>(null,null,HttpStatus.BAD_REQUEST);
        }
    }

}
