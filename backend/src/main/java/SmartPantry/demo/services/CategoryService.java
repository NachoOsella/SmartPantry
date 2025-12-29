package SmartPantry.demo.services;

import SmartPantry.demo.dtos.requests.CategoryRequest;
import SmartPantry.demo.dtos.responses.CategoryResponse;
import SmartPantry.demo.entities.Category;
import SmartPantry.demo.repositories.CategoryRepository;
import SmartPantry.demo.services.interfaces.ICategoryService;
import jakarta.transaction.Transactional;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing product categories.
 */
@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    /**
     * Retrieves all available categories.
     *
     * @return a list of {@link CategoryResponse} objects
     */
    @Override
    public List<CategoryResponse> getAll() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> CategoryResponse.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .build())
                .toList();
    }

    /**
     * Creates a new category.
     *
     * @param request the category details to be created
     * @return the created {@link CategoryResponse}
     * @throws IllegalArgumentException if a category with the same name already exists
     */
    @Override
    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("This category already exists");
        }
        Category category = Category.builder()
                .name(request.getName())
                .build();
        Category savedCategory = categoryRepository.save(category);
        return CategoryResponse.builder()
                .id(savedCategory.getId())
                .name(savedCategory.getName())
                .build();
    }
}
