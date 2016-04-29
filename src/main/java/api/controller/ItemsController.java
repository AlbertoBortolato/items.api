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
 */  /*@-rest-controller E' come il @Controller*/
@RestController
@RequestMapping("api")//Tutti i metodi sotto si aggiungono al requestmapping con "api"
public class ItemsController {


    @Inject
    ItemsService service;

    @RequestMapping(value = {"/", ""},
      method = RequestMethod.GET,
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}) /*Produces con gli array*/
    public ResponseEntity<Items> getItems(@RequestParam Map<String, String> queryParamsMap) {

        return new ResponseEntity<Items>(service.items(), null, HttpStatus.OK);
    }

    @RequestMapping(value = {"/", ""},
      method = RequestMethod.POST,
      produces = {MediaType.APPLICATION_JSON_VALUE},
      consumes = {MediaType.APPLICATION_JSON_VALUE}) /*Produces con gli array*/
    @ResponseBody
    ResponseEntity createItems(@RequestBody Items.Item item) {

        if (!service.items().getItems().contains(item)) { //se items NON CONTIENE item allora lo aggiungi
            service.items().getItems().add(item);
        }
        return new ResponseEntity<Items>(service.items(), null, HttpStatus.OK);
    }

    @RequestMapping(value = {"/", ""},
      method = RequestMethod.DELETE,
      produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    ResponseEntity deleteItems(@RequestBody Items.Item item) {

        if (service.items().getItems().contains(item)) { //se items NON CONTIENE item allora lo rimuovi
            service.items().getItems().remove(item);
        }
        return new ResponseEntity<Items>(service.items(), null, HttpStatus.OK);
    }

    @RequestMapping(value = {"/", ""},
      method = RequestMethod.PUT,
      produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    ResponseEntity updateItems(@RequestBody Items.Item item, Items.Item itemnew) {

        if (service.items().getItems().contains(item)) { //se items NON CONTIENE item allora lo rimuovi
            service.items().getItems().remove(item);
            service.items().getItems().add(itemnew);
        }
        return new ResponseEntity<Items>(service.items(), null, HttpStatus.OK);
    }

}

/*Viene usato il ResponseEntity ma con il @RequestParam mentre nell'esercizio degli items abbiamo usato @PathVariable
* */

/*Il service è uno strato di servizio che interagisce con un servizio ma, allo stesso tempo, potrebbe interagire con un
* altro servizio.*/
