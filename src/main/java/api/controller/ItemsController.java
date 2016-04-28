package api.controller;

import api.model.Items;
import api.service.ItemsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.Map;

/**
 * Created by matthew on 28.04.16.
 */  /*@-rest-controller E' come il @Controller*/
@RestController
@RequestMapping("api")
public class ItemsController {

    @Inject
    ItemsService service;

    @RequestMapping(value = {"/", ""},
    method = RequestMethod.GET,
    produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}) /*Produces con gli array*/
    public ResponseEntity<Items> getItems(@RequestParam Map<String, String> queryParamsMap){

        return new ResponseEntity<Items>(service.items(), null, HttpStatus.OK);
    }


}

/*Viene usato il ResponseEntity ma con il @RequestParam mentre nell'esercizio degli items abbiamo usato @PathVariable
* */
