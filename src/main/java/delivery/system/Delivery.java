package delivery.system;

import java.time.LocalDate;

public record Delivery(
        Long id,
        Long userId,
        Long product,
        Long executor,
        Long price,
        LocalDate date,
        DeliveryStatus status
) {
}
