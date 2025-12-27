package SmartPantry.demo.services;

import SmartPantry.demo.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduledTaskService {

    private final ProductRepository productRepository;

    /**
     * TO DO:
     * This task should run periodically (e.g., every midnight).
     * 1. Define the cron expression or fixedRate in @Scheduled.
     * 2. Find products that are near expiration (e.g., in 3 days)
     * and haven't been notified yet (notified = false).
     * 3. For each product:
     * - Log a message: "Product [Name] for user [Username] is expiring soon".
     * - In a real app, send an email or push notification.
     * - Update the product's 'notified' flag to true to avoid duplicate alerts.
     */
    @Scheduled(cron = "0 0 0 * * *") // Runs at midnight every day
    public void checkProductExpirations() {
        log.info("Starting background check for expiring products at {}", LocalDate.now());

        // 1. Fetch products needing notification
        // 2. Process notifications
        // 3. Save updated flags
    }
}
