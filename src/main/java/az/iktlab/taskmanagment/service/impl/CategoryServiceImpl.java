package az.iktlab.taskmanagment.service.impl;

import az.iktlab.taskmanagment.entity.Category;
import az.iktlab.taskmanagment.model.request.CategoryCreateRequest;
import az.iktlab.taskmanagment.repository.CategoryRepository;
import az.iktlab.taskmanagment.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    @Override
    public void addCategory(CategoryCreateRequest categoryCreateRequest) {
        Category category = Category.builder().name(categoryCreateRequest.getCategoryName()).build();
        categoryRepository.save(category);
    }
}
