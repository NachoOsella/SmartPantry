package SmartPantry.demo.services;

import SmartPantry.demo.dtos.requests.CategoryRequest;
import SmartPantry.demo.dtos.responses.CategoryResponse;
import SmartPantry.demo.entities.Category;
import SmartPantry.demo.exceptions.ResourceNotFoundException;
import SmartPantry.demo.repositories.CategoryRepository;
import SmartPantry.demo.services.interfaces.ICategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for managing product categories.
 */
@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;

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
        return mapToResponse(savedCategory);
    }

    /**
     * Updates an existing category.
     */
    @Override
    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));
        
        if (!category.getName().equalsIgnoreCase(request.getName()) && 
            categoryRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Category name already exists");
        }

        category.setName(request.getName());
        Category savedCategory = categoryRepository.save(category);
        return mapToResponse(savedCategory);
    }

    /**
     * Deletes a category.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category", id);
        }
        categoryRepository.deleteById(id);
    }

    private CategoryResponse mapToResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
