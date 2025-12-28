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

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<CategoryResponse> getAll() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> modelMapper.map(category, CategoryResponse.class))
                .toList();
    }

    @Override
    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("This category already exists");
        }
        Category category = modelMapper.map(request, Category.class);
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryResponse.class);
    }
}