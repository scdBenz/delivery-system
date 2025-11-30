package delivery.system;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class DeliveryService {

    private final Map<Long, Delivery> deliveryMap;
    private final AtomicLong idCounter;

    public DeliveryService() {
        deliveryMap = new HashMap<>();
        idCounter = new AtomicLong();
    }

    public Delivery getDeliveryById(Long id) {
        if(!deliveryMap.containsKey(id)){
            throw new NoSuchElementException("not found delivery by id = " + id);
        }
        return deliveryMap.get(id);
    }

    public List<Delivery> findAllDeliveries() {
        return deliveryMap.values().stream().toList();
    }

    public Delivery createDelivery(Delivery deliveryToCreate) {
        if(deliveryToCreate.id() != null){
            throw new IllegalArgumentException("Id should be empty");
        }
        if (deliveryToCreate.status() != null){
            throw new IllegalArgumentException("Id should be empty");
        }
        var newDelivery = new Delivery(
                idCounter.incrementAndGet(),
                deliveryToCreate.userId(),
                deliveryToCreate.product(),
                deliveryToCreate.executor(),
                deliveryToCreate.price(),
                deliveryToCreate.date(),
                DeliveryStatus.PENDING
        );
        deliveryMap.put(newDelivery.id(), newDelivery);
        return newDelivery;
    }


    public Delivery updateDelivery(Long id, Delivery deliveryToUpdate) {
        if (!deliveryMap.containsKey(id)){
            throw new NoSuchElementException("Not found delivery by id = "+ id);
        }
        var delivery = deliveryMap.get(id);
        if (delivery.status() != DeliveryStatus.PENDING){
            throw new IllegalStateException("Cannot modify delivery: status= "+ delivery);
        }
        var updateDelivery = new Delivery(
                delivery.id(),
                deliveryToUpdate.userId(),
                deliveryToUpdate.product(),
                deliveryToUpdate.executor(),
                deliveryToUpdate.price(),
                deliveryToUpdate.date(),
                DeliveryStatus.PENDING
        );
        deliveryMap.put(delivery.id(), updateDelivery);
        return updateDelivery;
    }

    public void deleteDelivery(Long id) {
        if (!deliveryMap.containsKey(id)){
            throw new NoSuchElementException("Not found delivery by id = "+ id);
        }
        deliveryMap.remove(id);
    }

    public Delivery approveDelivery(Long id) {
        if (!deliveryMap.containsKey(id)){
            throw new NoSuchElementException("Not found delivery by id = "+ id);
        }
        var delivery = deliveryMap.get(id);
        if (delivery.status() != DeliveryStatus.PENDING){
            throw new IllegalStateException("Cannot approve delivery status= "+delivery);
        }
        var isConflict = isDeliveryConflict(delivery);
        if (isConflict){
            throw new IllegalStateException("Cannot approve delivery because of conflict");
        }
        var approvedDelivery = new Delivery(
                delivery.id(),
                delivery.userId(),
                delivery.product(),
                delivery.executor(),
                delivery.price(),
                delivery.date(),
                DeliveryStatus.APPROVED
        );
        deliveryMap.put(delivery.id(), approvedDelivery);
        return approvedDelivery;
    }

    private boolean isDeliveryConflict(Delivery delivery){
        return false;
    }
}
