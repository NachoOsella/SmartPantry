package SmartPantry.demo.services;

import SmartPantry.demo.dtos.requests.ProductRequest;
import SmartPantry.demo.dtos.responses.ProductResponse;
import SmartPantry.demo.entities.enums.ExpiryStatus;
import SmartPantry.demo.repositories.CategoryRepository;
import SmartPantry.demo.repositories.ProductRepository;
import SmartPantry.demo.services.interfaces.IProductService;
import SmartPantry.demo.services.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final IUserService userService;
    private final ModelMapper modelMapper;

    /**
     * TO DO:
     * 1. Get the current logged-in user from userService.
     * 2. Use productRepository.findByUser() to get entities.
     * 3. Map entities to ProductResponse list.
     * 4. Calculate 'daysRemaining' and 'expiryStatus' during mapping or in a helper method.
     */
    @Override
    public List<ProductResponse> getAllForCurrentUser() {
        return null;
    }

    /**
     * TO DO:
     * 1. Find product by ID.
     * 2. Verify if the product belongs to the current user.
     * 3. Return mapped ProductResponse or throw EntityNotFoundException.
     */
    @Override
    public ProductResponse getById(Long id) {
        return null;
    }

    /**
     * TO DO:
     * 1. Map ProductRequest to Product entity.
     * 2. Assign current user to the product.
     * 3. Find and assign category if categoryId is present.
     * 4. Save and return mapped ProductResponse.
     */
    @Override
    public ProductResponse create(ProductRequest request) {
        return null;
    }

    /**
     * TO DO:
     * 1. Find existing product.
     * 2. Verify ownership.
     * 3. Update fields from request.
     * 4. Save and return mapped response.
     */
    @Override
    public ProductResponse update(Long id, ProductRequest request) {
        return null;
    }

    /**
     * TO DO:
     * 1. Find product and verify ownership.
     * 2. Delete from repository.
     */
    @Override
    public void delete(Long id) {
    }

    /**
     * TO DO:
     * 1. Get current user.
     * 2. Based on ExpiryStatus enum, call specific repository methods:
     *    - RED: findByUserAndExpirationDateBefore(today)
     *    - YELLOW: findByUserAndExpirationDateBetween(today, nextWeek)
     *    - GREEN: findByUserAndExpirationDateAfter(nextWeek)
     */
    @Override
    public List<ProductResponse> getByStatus(ExpiryStatus status) {
        return null;
    }
}
