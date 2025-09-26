package com.odiaz.moneymanager.controller;

import com.odiaz.moneymanager.dto.category.CategoryDTO;
import com.odiaz.moneymanager.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<?> listCategories(){
        List<CategoryDTO> categories = categoryService.getCategories();
        return ResponseEntity.ok().body(categories);
    }

    @PostMapping
    public ResponseEntity<?> saveCategory(@RequestBody CategoryDTO body){
        CategoryDTO category = categoryService.saveCategory(body);
        return ResponseEntity.ok().body(category);
    }

    @GetMapping("/{type}")
    public ResponseEntity<?> listCategoriesByType(@PathVariable String type){
        List<CategoryDTO> categories = categoryService.getCategoriesByTypeForCurrentUser(type);
        return ResponseEntity.ok().body(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Integer id, @RequestBody CategoryDTO body){
        CategoryDTO category = categoryService.updateCategory(id, body);
        return ResponseEntity.ok().body(category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer id){
       categoryService.deleteCategory(id);
       return ResponseEntity.ok().body(Map.of("message", "Categoria eliminada correctamente"));
    }

}
