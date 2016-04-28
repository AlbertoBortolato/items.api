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
 */
@RestController
@RequestMapping("api") // questo permette che nel controller possiamo aggiungere altri controller
public class ItemsController {

    @Inject
    ItemsService service;

    @RequestMapping(value = {"/", ""},   //array per request mapping mappiamo sia barra che stringa vuota al percorso.
    method = RequestMethod.GET,
    produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})  // produces come un l'array
    public ResponseEntity<Items> getItems(@RequestParam Map<String, String> queryParamsMap){ //request param con una mappa generica per chiave e valore.

        return new ResponseEntity<Items>(service.items(), null, HttpStatus.OK);   //service.items() restituisce la bean di items.
    }


}
