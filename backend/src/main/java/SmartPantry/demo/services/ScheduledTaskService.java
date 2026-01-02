package SmartPantry.demo.services;

import SmartPantry.demo.entities.Product;
import SmartPantry.demo.repositories.ProductRepository;
import SmartPantry.demo.entities.enums.ExpiryStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

            // 1. Fetch all products
            List<Product> allProducts = productRepository.findAll();

            int updatedCount = 0;
            for (Product product : allProducts) {
                ExpiryStatus newStatus = calculateExpiryStatus(product.getExpirationDate(), today);

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
     * Calculates expiry status based on product's expiration date.
     * RED: expired (< 0 days remaining)
     * YELLOW: expires within 7 days (including today)
     * GREEN: expires after 7 days
     *
     * @param expirationDate expiration date of product
     * @param today current date
     * @return calculated expiry status
     */
    private ExpiryStatus calculateExpiryStatus(LocalDate expirationDate, LocalDate today) {
        long daysRemaining = ChronoUnit.DAYS.between(today, expirationDate);

        if (daysRemaining < 0) {
            return ExpiryStatus.RED;
        } else if (daysRemaining <= 7) {
            return ExpiryStatus.YELLOW;
        } else {
            return ExpiryStatus.GREEN;
        }
    }
}
