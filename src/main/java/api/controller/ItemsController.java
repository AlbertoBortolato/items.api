package api.controller;

import api.model.Items;
import api.service.ItemsService;
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
    public ResponseEntity<Items> getItems(@RequestParam Map<String, String> queryParamsMap){

        return new ResponseEntity<Items>(service.items(), null, HttpStatus.OK);
    }


    //curl -X POST -H "Content-Type: application/json" -d @test.txt http://admin:admin-1@localhost:8080/api
    @RequestMapping(value = {"/", ""},
      method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Items> getItemsPost(@RequestParam Map<String, String> queryParamsMap,
                                              @RequestBody Items.Item item){
        service.items().getItems().add(item);

        return new ResponseEntity<Items>(service.items(), null, HttpStatus.OK);
    }

}
