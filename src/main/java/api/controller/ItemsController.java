package api.controller;

import api.model.Items;
import api.service.ItemsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * Created by matthew on 28.04.16.
 */
@RestController
@RequestMapping("api")                          //RequestMapping successivi partono da api/
public class ItemsController {


    @Inject
    ItemsService service;

    @RequestMapping(value = {"/", ""},          //mappiamo lo stesso metodo a più percorsi
      method = RequestMethod.GET,
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Items> getItems(@RequestParam Map<String, String> queryParamsMap) {

        return new ResponseEntity<Items>(service.items(), null, HttpStatus.OK);
    }

    @RequestMapping(value = {"/"},           //attributo che aggiungiamo sul URL
      method = RequestMethod.POST,
      consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},     //Di default lo prende (Specificato)
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    //Di default lo prende (Specificato). Su qualsiasi metodo che restituisce qualcosa
    @ResponseBody
    public ResponseEntity<List<Items.Item>> postRequest(@RequestBody List<Items.Item> items) { //@RequestBody:  corpo della richiesta

        if (items != null) {              // this.service.addSingle(item);
            service.addAll(items);
            return new ResponseEntity<List<Items.Item>>(items, null, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<List<Items.Item>>(items, null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{id}",       //attributo che aggiungiamo sul URL
      method = RequestMethod.DELETE,
      //consumes = MediaType.APPLICATION_JSON_VALUE,  Dichiariamo il contentType e header Accept, in questo caso nessun corpo viene "consumato"
      produces = MediaType.APPLICATION_JSON_VALUE)  //              lato SERVER     lato CLIENT
    @ResponseBody
    public ResponseEntity<Items> deleteItem(@PathVariable("id") String id) {

        if(id!=null) {
            this.service.deleteById(id);
            return new ResponseEntity<Items>(service.items(), null, HttpStatus.OK);
        } else {
            return new ResponseEntity<Items>(service.items(), null, HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(value = "/{id}",           //attributo che aggiungiamo sul URL
      method = RequestMethod.PUT,
      consumes = MediaType.APPLICATION_JSON_VALUE,     //Di default lo prende (Specificato)
      produces = MediaType.APPLICATION_JSON_VALUE)
    //Di default lo prende (Specificato). Su qualsiasi metodo che restituisce qualcosa
    @ResponseBody
    public ResponseEntity<Items> putRequest(@RequestBody Items.Item updated, @PathVariable("id") String id) { //@RequestBody:  corpo della richiesta
        if(id!=null) {
            Items.Item toUpdate = new Items.Item();
            toUpdate.setId(id);
            this.service.updateObject(updated, toUpdate);
            return new ResponseEntity<Items>(service.items(), null, HttpStatus.OK);
        }else {
            return new ResponseEntity<Items>(service.items(), null, HttpStatus.BAD_REQUEST);
        }

    }
}
