package api.controller;

import api.model.Items;
import api.service.ItemsService;
import org.apache.tomcat.util.http.fileupload.MultipartStream;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by matthew on 28.04.16.
 */
@RestController
@RequestMapping("api") // questo permette che nel controller possiamo aggiungere altri controller
public class ItemsController {

    @Inject
    ItemsService service;

    //$ curl -X GET http://admin:admin-1@localhost:8080/api
    @RequestMapping(value = {"/", ""},   //array per request mapping mappiamo sia barra che stringa vuota al percorso.
    method = RequestMethod.GET,
    produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})  // produces come un l'array
    public ResponseEntity<Items> getItems(@RequestParam Map<String, String> queryParamsMap){ //request param con una mappa generica per chiave e valore.

        return new ResponseEntity<Items>(service.items(), null, HttpStatus.OK);   //service.items() restituisce la bean di items.
    }

    //$ curl -X POST -d @dati.json -H "Content-Type: application/json" http://admin:admin-1@localhost:8080/api
    @RequestMapping(
    value = {"/", ""},
    method = RequestMethod.POST,
    produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Items> postItems(@RequestBody Items.Item item){ //request param con una mappa generica per chiave e valore.

        service.items().getItems().add(item);

        return new ResponseEntity<Items>(service.items(), null, HttpStatus.OK);   //service.items() restituisce la bean di items.
    }

    //$ curl -X DELETE -H "Content-Type: application/json" http://admin:admin-1@localhost:8080/api/ss
    @RequestMapping(
      value = {"/{id}", ""},
      method = RequestMethod.DELETE,
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
      consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
      public ResponseEntity<Items> deleteItems(@PathVariable("id") String id){ //request param con una mappa generica per chiave e valore.
            Items.Item item = new Items.Item();
            item.setId(id);
            if(service.items().getItems().contains(item)) {
                service.items().getItems().remove(item);
            }
        return new ResponseEntity<Items>(service.items(), null, HttpStatus.OK);   //service.items() restituisce la bean di items.
    }

    //$ curl -X PUT -d @dati.json -H "Content-Type: application/json" http://admin:admin-1@localhost:8080/api/ss
    @RequestMapping(
      value = {"/{id}", ""},
      method = RequestMethod.PUT,
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
      consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
      public ResponseEntity<Items> putItems(@RequestBody Items.Item item, @PathVariable("id") String id){ //request param con una mappa generica per chiave e valore.
        Items.Item temporaneo = new Items.Item();
        temporaneo.setId(id);

        if(service.items().getItems().contains(temporaneo)) {
            service.items().getItems().remove(temporaneo);
            service.items().getItems().add(item);
        }

        return new ResponseEntity<Items>(service.items(), null, HttpStatus.OK);   //service.items() restituisce la bean di items.
    }


    //$ curl -X GET -H "Content-Type: application/json" http://admin:admin-1@localhost:8080/api/ciao/?query=sfdf
    @RequestMapping(
      value = {"/", "ciao"},  // ho un altro metodo get rinimino su value per poterli usare entrambi.
      method = RequestMethod.GET,
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})

    public ResponseEntity<Items> ricercaItems(@RequestParam(value = "query", required = false) String query) { //request param con una mappa generica per chiave e valore.
        Items.Item item = new Items.Item();
        item.setId(query);

        if (service.items().getItems().contains(item)) {
            List<Items.Item> tmpList = new LinkedList<>(service.items().getItems());
            Items.Item result = tmpList.get(tmpList.indexOf(item));
            return new ResponseEntity(result, null, HttpStatus.OK);

        }
        return new ResponseEntity<Items>(service.items(), null, HttpStatus.OK);
    }

           //service.items() restituisce la bean di items.


}
