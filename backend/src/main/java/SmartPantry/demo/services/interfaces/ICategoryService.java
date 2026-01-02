package SmartPantry.demo.services.interfaces;

import SmartPantry.demo.dtos.requests.CategoryRequest;
import SmartPantry.demo.dtos.responses.CategoryResponse;

import java.util.List;

public interface ICategoryService {
    List<CategoryResponse> getAll();
    CategoryResponse create(CategoryRequest request);
    CategoryResponse update(Long id, CategoryRequest request);
    void delete(Long id);
}
