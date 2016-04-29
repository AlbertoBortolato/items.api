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
    public ResponseEntity<Items> getItems(@RequestParam Map<String, String> queryParamsMap){

        return new ResponseEntity<Items>(service.items(), null, HttpStatus.OK);
    }

    @RequestMapping(value = {"/", ""}, method = RequestMethod.POST,
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
      consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Items.Item> create(@RequestBody Items.Item item1){
        if(service.pullItem(item1)){
            return new ResponseEntity<Items.Item>(item1, null, HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity<Items.Item>(item1, null, HttpStatus.BAD_REQUEST);
        }
    }


}
