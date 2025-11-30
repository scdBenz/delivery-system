package delivery.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {
    private static final Logger log = LoggerFactory.getLogger(DeliveryController.class);

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Delivery> getDeliveryById(
            @PathVariable("id") Long id
    ){
        log.info("Called getDeliveryById: id= "+ id);
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(deliveryService.getDeliveryById(id));
        }catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping
    public ResponseEntity<List<Delivery>> getAllDeliveries(){
        log.info("Called getAllDeliveries");
        return ResponseEntity.ok(deliveryService.findAllDeliveries());
    }

    @PostMapping
    public ResponseEntity<Delivery> createDelivery (
           //@RequestBody берет тело HTTP-запроса
           // и конвертирует его в объект java, который указан в параметрах метода контроллера.
           // Чаще всего это JSON
            @RequestBody Delivery deliveryToCreate
    ){
        log.info("Called createDelivery");
        return ResponseEntity.status(HttpStatus.CREATED)
//                .header("test-header", "123")
                .body(deliveryService.createDelivery(deliveryToCreate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Delivery> updateDelivery(
            @PathVariable("id") Long id,
            @RequestBody Delivery deliveryToUpdate
    ){
        log.info("Called updateDelivery id={}, deliveryToUpdate={}", id, deliveryToUpdate);
        var updated = deliveryService.updateDelivery(id, deliveryToUpdate);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDelivery(
            @PathVariable("id") Long id
    ){
        log.info("Called deleteDelivery: id={}", id);
        try{
            deliveryService.deleteDelivery(id);
            return ResponseEntity.ok()
                    .build();
        }catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .build();
        }

    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Delivery> approveDelivery(
            @PathVariable("id") Long id

    ){
        log.info("Called approveDelivery: id={}", id);
        var delivery = deliveryService.approveDelivery(id);
        return ResponseEntity.ok(delivery);
    }
}
