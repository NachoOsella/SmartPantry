package SmartPantry.demo.dtos.responses;

import SmartPantry.demo.entities.enums.ExpiryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private LocalDate expirationDate;
    private int quantity;
    private String categoryName;
    private ExpiryStatus expiryStatus;
    private long daysRemaining;
}
