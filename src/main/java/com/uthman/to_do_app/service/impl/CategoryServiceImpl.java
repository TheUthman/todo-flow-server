package com.uthman.to_do_app.service.impl;

import com.uthman.to_do_app.dto.category.CategoryRequest;
import com.uthman.to_do_app.dto.category.CategoryResponse;
import com.uthman.to_do_app.entity.Category;
import com.uthman.to_do_app.entity.User;
import com.uthman.to_do_app.exception.ResourceNotFoundException;
import com.uthman.to_do_app.repository.CategoryRepository;
import com.uthman.to_do_app.repository.UserRepository;
import com.uthman.to_do_app.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String idStr = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Current user ID from context: {}", idStr);
        try {
            Long id = Long.parseLong(idStr);
            return userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        } catch (NumberFormatException e) {
            return userRepository.findByUsername(idStr)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + idStr));
        }
    }

    private CategoryResponse map(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .color(category.getColor())
                .build();
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        User user = getCurrentUser();
        return categoryRepository.findByUser(user)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        User user = getCurrentUser();
        log.info("Fetching category ID: {} for user: {}", id, user.getUsername());
        Category category = categoryRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found or unauthorized"));
        return map(category);
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = Category.builder()
                .name(request.getName())
                .color(request.getColor())
                .user(getCurrentUser())
                .build();

        categoryRepository.save(category);
        return map(category);
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findByIdAndUser(id, getCurrentUser())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found or unauthorized"));

        category.setName(request.getName());
        category.setColor(request.getColor());
        categoryRepository.save(category);
        return map(category);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findByIdAndUser(id, getCurrentUser())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found or unauthorized"));
        categoryRepository.delete(category);
    }
}
