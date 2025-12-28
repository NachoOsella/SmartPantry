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

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final IUserService userService;
    private final ModelMapper modelMapper;

    @Override
    public List<ProductResponse> getAllForCurrentUser() {
        User currentUser = userService.getCurrentUserEntity();
        List<Product> products = productRepository.findByUser(currentUser);
        return products.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ProductResponse getById(Long id) {
        Product product = findProductById(id);
        verifyOwnership(product);

        return mapToResponse(product);
    }

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

    @Override
    public void delete(Long id) {
        Product existingProduct = findProductById(id);
        verifyOwnership(existingProduct);

        productRepository.delete(existingProduct);
    }

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

    private List<Product> getProductsByStatus(User currentUser, ExpiryStatus status, LocalDate today, LocalDate nextWeek) {
        return switch (status) {
            case RED -> productRepository.findByUserAndExpirationDateBefore(currentUser, today);
            case YELLOW -> productRepository.findByUserAndExpirationDateBetween(currentUser, today, nextWeek);
            case GREEN -> productRepository.findByUserAndExpirationDateAfter(currentUser, nextWeek);
        };
    }

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

    private ExpiryStatus calculateExpiryStatus(long daysRemaining) {
        if (daysRemaining < 0) {
            return ExpiryStatus.RED;
        } else if (daysRemaining <= 7) {
            return ExpiryStatus.YELLOW;
        } else {
            return ExpiryStatus.GREEN;
        }
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
    }

    private void verifyOwnership(Product product) {
        Long currentUserId = userService.getCurrentUserEntity().getId();
        if (!product.getUser().getId().equals(currentUserId)) {
            throw new UnauthorizedAccessException("Product", product.getId(), currentUserId);
        }
    }
}