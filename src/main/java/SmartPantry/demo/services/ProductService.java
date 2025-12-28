package SmartPantry.demo.services;

import SmartPantry.demo.dtos.requests.ProductRequest;
import SmartPantry.demo.dtos.responses.ProductResponse;
import SmartPantry.demo.entities.Product;
import SmartPantry.demo.entities.User;
import SmartPantry.demo.entities.enums.ExpiryStatus;
import SmartPantry.demo.exceptions.ResourceNotFoundException;
import SmartPantry.demo.exceptions.UnauthorizedAccessException;
import SmartPantry.demo.repositories.CategoryRepository;
import SmartPantry.demo.repositories.ProductRepository;
import SmartPantry.demo.services.interfaces.IProductService;
import SmartPantry.demo.services.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Service implementation for managing product operations within the pantry.
 * Handles CRUD operations and expiration status logic.
 */
@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final IUserService userService;
    private final ModelMapper modelMapper;

    /**
     * Retrieves all products belonging to the currently authenticated user.
     *
     * @return a list of {@link ProductResponse} with calculated expiration status
     */
    @Override
    public List<ProductResponse> getAllForCurrentUser() {
        User currentUser = userService.getCurrentUserEntity();
        List<Product> products = productRepository.findByUser(currentUser);
        return products.stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Retrieves a specific product by its ID, verifying ownership.
     *
     * @param id the ID of the product to retrieve
     * @return the {@link ProductResponse} with calculated expiration status
     * @throws ResourceNotFoundException if the product does not exist
     * @throws UnauthorizedAccessException if the product does not belong to the current user
     */
    @Override
    public ProductResponse getById(Long id) {
        Product product = findProductById(id);
        verifyOwnership(product);

        return mapToResponse(product);
    }

    /**
     * Creates a new product for the currently authenticated user.
     *
     * @param request the product details to be created
     * @return the created {@link ProductResponse}
     * @throws ResourceNotFoundException if the provided category ID does not exist
     */
    @Override
    public ProductResponse create(ProductRequest request) {
        User currentUser = userService.getCurrentUserEntity();

        Product product = modelMapper.map(request, Product.class);
        product.setUser(currentUser);

        if (request.getCategoryId() != null) {
            categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", request.getCategoryId()));
            categoryRepository.findById(request.getCategoryId()).ifPresent(product::setCategory);
        }

        Product savedProduct = productRepository.save(product);
        return mapToResponse(savedProduct);
    }

    /**
     * Updates an existing product, verifying ownership.
     *
     * @param id the ID of the product to update
     * @param request the new details for the product
     * @return the updated {@link ProductResponse}
     * @throws ResourceNotFoundException if the product or category does not exist
     * @throws UnauthorizedAccessException if the product does not belong to the current user
     */
    @Override
    public ProductResponse update(Long id, ProductRequest request) {
        Product existingProduct = findProductById(id);
        verifyOwnership(existingProduct);

        modelMapper.map(request, existingProduct);

        if (request.getCategoryId() != null) {
            categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", request.getCategoryId()));
            categoryRepository.findById(request.getCategoryId()).ifPresent(existingProduct::setCategory);
        }

        Product updatedProduct = productRepository.save(existingProduct);
        return mapToResponse(updatedProduct);
    }

    /**
     * Deletes a product by its ID, verifying ownership.
     *
     * @param id the ID of the product to delete
     * @throws ResourceNotFoundException if the product does not exist
     * @throws UnauthorizedAccessException if the product does not belong to the current user
     */
    @Override
    public void delete(Long id) {
        Product existingProduct = findProductById(id);
        verifyOwnership(existingProduct);

        productRepository.delete(existingProduct);
    }

    /**
     * Retrieves products filtered by their expiration status for the current user.
     *
     * @param status the {@link ExpiryStatus} to filter by
     * @return a list of products matching the status
     */
    @Override
    public List<ProductResponse> getByStatus(ExpiryStatus status) {
        User currentUser = userService.getCurrentUserEntity();
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusDays(7);

        List<Product> products = getProductsByStatus(currentUser, status, today, nextWeek);

        return products.stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Internal helper to fetch products based on expiration logic.
     */
    private List<Product> getProductsByStatus(User currentUser, ExpiryStatus status, LocalDate today, LocalDate nextWeek) {
        return switch (status) {
            case RED -> productRepository.findByUserAndExpirationDateBefore(currentUser, today);
            case YELLOW -> productRepository.findByUserAndExpirationDateBetween(currentUser, today, nextWeek);
            case GREEN -> productRepository.findByUserAndExpirationDateAfter(currentUser, nextWeek);
        };
    }

    /**
     * Maps a Product entity to a ProductResponse DTO, calculating dynamic fields.
     */
    private ProductResponse mapToResponse(Product product) {
        long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), product.getExpirationDate());
        ExpiryStatus expiryStatus = calculateExpiryStatus(daysRemaining);

        ProductResponse response = modelMapper.map(product, ProductResponse.class);
        response.setDaysRemaining(daysRemaining);
        response.setExpiryStatus(expiryStatus);

        if (product.getCategory() != null) {
            response.setCategoryName(product.getCategory().getName());
        }

        return response;
    }

    /**
     * Logic to determine the expiration status based on days remaining.
     */
    private ExpiryStatus calculateExpiryStatus(long daysRemaining) {
        if (daysRemaining < 0) {
            return ExpiryStatus.RED;
        } else if (daysRemaining <= 7) {
            return ExpiryStatus.YELLOW;
        } else {
            return ExpiryStatus.GREEN;
        }
    }

    /**
     * Internal helper to find a product or throw an exception.
     */
    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
    }

    /**
     * Internal helper to verify if the authenticated user owns the resource.
     */
    private void verifyOwnership(Product product) {
        Long currentUserId = userService.getCurrentUserEntity().getId();
        if (!product.getUser().getId().equals(currentUserId)) {
            throw new UnauthorizedAccessException("Product", product.getId(), currentUserId);
        }
    }
}
