package com.odiaz.moneymanager.dto.profile;

import com.odiaz.moneymanager.model.ProfileEntity;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {

    public ProfileEntity toEntity(ProfileDTO dto){
        return ProfileEntity.builder()
                .id(dto.getId())
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .profileImageUrl(dto.getProfileImageUrl())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

    public ProfileDTO toDTO(ProfileEntity entity){
        return ProfileDTO.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .email(entity.getEmail())
                .profileImageUrl(entity.getProfileImageUrl())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

}
