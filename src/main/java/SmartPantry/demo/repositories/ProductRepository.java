package SmartPantry.demo.repositories;

import SmartPantry.demo.entities.Category;
import SmartPantry.demo.entities.Product;
import SmartPantry.demo.entities.User;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // User-specific queries
    List<Product> findByUser(User user);
    List<Product> findByUserAndCategory(User user, Category category);

    // Status-based queries for User
    List<Product> findByUserAndExpirationDateBefore(User user, LocalDate date);
    List<Product> findByUserAndExpirationDateBetween(User user, LocalDate start,
                                                     LocalDate end);
    List<Product> findByUserAndExpirationDateAfter(User user, LocalDate date);

    // Global queries for Scheduled Tasks
    List<Product> findByExpirationDateBeforeAndNotifiedFalse(LocalDate date);
}
