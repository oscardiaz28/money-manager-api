package com.odiaz.moneymanager.service;

import com.odiaz.moneymanager.dto.category.CategoryDTO;
import com.odiaz.moneymanager.dto.category.CategoryMapper;
import com.odiaz.moneymanager.model.CategoryEntity;
import com.odiaz.moneymanager.model.ProfileEntity;
import com.odiaz.moneymanager.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final ProfileService profileService;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(ProfileService profileService, CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.profileService = profileService;
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public List<CategoryDTO> getCategories(){
        ProfileEntity profile = profileService.getUserLogged();
        List<CategoryEntity> categories = profile.getCategories();
        return categories.stream()
                .map( categoryMapper::toDto )
                .toList();
    }

    public CategoryDTO saveCategory(CategoryDTO body) {
        ProfileEntity profile = profileService.getUserLogged();
        if( categoryRepository.existsByNameAndProfileId(body.getName(), profile.getId()) ){
            throw new RuntimeException("Category with this name already exist");
        }
        CategoryEntity category = categoryMapper.toEntity(body, profile);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    public List<CategoryDTO> getCategoriesByTypeForCurrentUser(String type){
        ProfileEntity profile = profileService.getUserLogged();
        List<CategoryEntity> categories = categoryRepository.findByTypeAndProfileId(type, profile.getId());
        return categories.stream()
                .map( categoryMapper::toDto )
                .toList();
    }

    public CategoryDTO updateCategory(Integer categoryId, CategoryDTO body){
        ProfileEntity profile = profileService.getUserLogged();
        CategoryEntity existingCategory = categoryRepository.findByIdAndProfileId(categoryId, profile.getId())
                .orElseThrow( () -> new RuntimeException("Category not found") );
        existingCategory.setName(body.getName());
        existingCategory.setIcon(body.getIcon());
        existingCategory.setType(body.getType());
        CategoryEntity updated = categoryRepository.save(existingCategory);

        return categoryMapper.toDto(updated);
    }

}
