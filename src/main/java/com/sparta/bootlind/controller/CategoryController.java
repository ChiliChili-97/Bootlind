package com.sparta.bootlind.controller;

import com.sparta.bootlind.security.UserDetailsImpl;
import com.sparta.bootlind.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/category")
    @Operation(summary = "카테고리 목록 조회", description = "전체 카테고리 목록을 조회한다.")
    public List<String> getCategoryList(){
        return categoryService.getCategoryList();
    }

    @PostMapping("/category/{categoryname}")
    @Operation(summary = "카테고리 추가", description = "카테고리를 추가한다.")
    public String addCategory(@PathVariable String categoryname, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return categoryService.addCategory(categoryname, userDetails.getUser());
    }

    @PutMapping("/category/{categoryname}/{newcategoryname}")
    @Operation(summary = "카테고리 수정", description = "카테고리를 수정한다.")
    public String updateCategory(@PathVariable String categoryname, @PathVariable String newcategoryname, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return categoryService.updateCategory(categoryname, newcategoryname, userDetails.getUser());
    }

    @DeleteMapping("/category/{categoryname}")
    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제한다.")
    public String deleteCategory(@PathVariable String categoryname, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return categoryService.deleteCategory(categoryname, userDetails.getUser());
    }
}
