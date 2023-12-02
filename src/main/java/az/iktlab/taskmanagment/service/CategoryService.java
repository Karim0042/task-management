package az.iktlab.taskmanagment.service;

import az.iktlab.taskmanagment.model.request.CategoryCreateRequest;

public interface CategoryService{
    void addCategory(CategoryCreateRequest categoryCreateRequest);
}
