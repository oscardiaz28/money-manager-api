package com.odiaz.moneymanager.dto.category;

import com.odiaz.moneymanager.model.CategoryEntity;
import com.odiaz.moneymanager.model.ProfileEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryEntity toEntity(CategoryDTO dto, ProfileEntity profile){
        return CategoryEntity.builder()
                .name(dto.getName())
                .icon(dto.getIcon())
                .profile(profile)
                .type(dto.getType())
                .build();
    }

    public CategoryDTO toDto(CategoryEntity category){
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .icon(category.getIcon())
                .type(category.getType())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .profileId(category.getProfile().getId())
                .build();
    }



}
