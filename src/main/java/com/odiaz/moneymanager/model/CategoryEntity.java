package com.odiaz.moneymanager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime updatedAt;
    private String type;
    private String icon;
    @ManyToOne()
    @JoinColumn(name = "profile_id", nullable = false)
    private ProfileEntity profile;
}
