package SmartPantry.demo.services;

import SmartPantry.demo.dtos.requests.CategoryRequest;
import SmartPantry.demo.dtos.responses.CategoryResponse;
import SmartPantry.demo.repositories.CategoryRepository;
import SmartPantry.demo.services.interfaces.ICategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    /**
     * TO DO:
     * 1. Fetch all categories from categoryRepository.
     * 2. Map to CategoryResponse list.
     */
    @Override
    public List<CategoryResponse> getAll() {
        return null;
    }

    /**
     * TO DO:
     * 1. Check if category name already exists.
     * 2. Map request to entity.
     * 3. Save and return mapped response.
     */
    @Override
    public CategoryResponse create(CategoryRequest request) {
        return null;
    }
}
