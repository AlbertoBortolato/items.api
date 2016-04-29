package api.controller;

import api.model.Items;
import api.service.ItemsService;
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
    public ResponseEntity<Items> getItems(@RequestParam Map<String, String> queryParamsMap){

        return new ResponseEntity<Items>(service.items(), null, HttpStatus.OK);
    }


    //curl -X POST -H "Content-Type: application/json" -d @test.txt http://admin:admin-1@localhost:8080/api
    @RequestMapping(value = {"/", ""},
      method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Items> addItemPost(@RequestParam Map<String, String> queryParamsMap,
                                              @RequestBody Items.Item item){
        service.items().getItems().add(item);

        return new ResponseEntity<Items>(service.items(), null, HttpStatus.OK);
    }

    //curl -X DELETE -H "Content-Type: application/json" http://admin:admin-1@localhost:8080/api/{name}
    @RequestMapping(value={"/{name}",""},
    method = RequestMethod.DELETE,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> deleteItem(@RequestParam Map<String,String> queryParamsMap,
                                        @PathVariable("name") String name){
        for(Items.Item it: service.items().getItems()){
            if (it.getName().equals(name)){
                service.items().getItems().remove(it);
                return new ResponseEntity<Object>(true,null,HttpStatus.OK);
            }
        }
        return new ResponseEntity<Object>(false,null,HttpStatus.NO_CONTENT);
    }


}
