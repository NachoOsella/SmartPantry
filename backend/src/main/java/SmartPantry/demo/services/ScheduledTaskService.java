package SmartPantry.demo.services;

import SmartPantry.demo.entities.Product;
import SmartPantry.demo.repositories.ProductRepository;
import SmartPantry.demo.entities.enums.ExpiryStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Service responsible for executing background tasks, such as checking for expiring products.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduledTaskService {

    private final ProductRepository productRepository;

    /**
     * Background task that runs periodically to check for products nearing their expiration date.
     * Configured to run at midnight every day.
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void checkProductExpirations() {
        log.info("Starting background check for expiring products at {}", LocalDate.now());

        try {
            LocalDate today = LocalDate.now();
            LocalDate threeDaysFromNow = today.plusDays(3);
            LocalDate expiredDate = today.minusDays(1);

            // 1. Fetch products needing status update
            List<Product> allProducts = productRepository.findAll();

            int updatedCount = 0;
            for (Product product : allProducts) {
                ExpiryStatus newStatus = calculateExpiryStatus(product.getExpirationDate(), today, threeDaysFromNow, expiredDate);

                if (!newStatus.equals(product.getExpiryStatus())) {
                    product.setExpiryStatus(newStatus);
                    productRepository.save(product);
                    updatedCount++;
                }
            }

            log.info("Expiry status check completed. Updated {} products at {}", updatedCount, LocalDate.now());
        } catch (Exception e) {
            log.error("Error during product expiry check", e);
        }
    }

    /**
     * Calculates the expiry status based on the product's expiration date.
     *
     * @param expirationDate the expiration date of the product
     * @param today the current date
     * @param threeDaysFromNow date three days in the future
     * @param expiredDate one day in the past (to account for timezone issues)
     * @return the calculated expiry status
     */
    private ExpiryStatus calculateExpiryStatus(LocalDate expirationDate, LocalDate today,
                                                   LocalDate threeDaysFromNow, LocalDate expiredDate) {
        if (expirationDate.isBefore(expiredDate) || expirationDate.isEqual(expiredDate)) {
            return ExpiryStatus.RED;
        } else if (expirationDate.isBefore(today)) {
            return ExpiryStatus.RED;
        } else if (expirationDate.isEqual(today)) {
            return ExpiryStatus.YELLOW;
        } else if (expirationDate.isBefore(threeDaysFromNow) || expirationDate.isEqual(threeDaysFromNow)) {
            return ExpiryStatus.YELLOW;
        } else {
            return ExpiryStatus.GREEN;
        }
    }
}
