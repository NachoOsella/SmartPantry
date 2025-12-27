package SmartPantry.demo.services.interfaces;

import SmartPantry.demo.dtos.requests.ProductRequest;
import SmartPantry.demo.dtos.responses.ProductResponse;
import SmartPantry.demo.entities.enums.ExpiryStatus;

import java.util.List;

public interface IProductService {
    List<ProductResponse> getAllForCurrentUser();
    ProductResponse getById(Long id);
    ProductResponse create(ProductRequest request);
    ProductResponse update(Long id, ProductRequest request);
    void delete(Long id);
    List<ProductResponse> getByStatus(ExpiryStatus status);
}
