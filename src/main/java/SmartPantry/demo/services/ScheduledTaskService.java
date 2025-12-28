package SmartPantry.demo.services;

import SmartPantry.demo.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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
     * Currently configured to run at midnight every day.
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void checkProductExpirations() {
        log.info("Starting background check for expiring products at {}", LocalDate.now());

        // 1. Fetch products needing notification
        // 2. Process notifications
        // 3. Save updated flags
    }
}
