package com.sparta.bootlind.controller;

import com.sparta.bootlind.dto.requestDto.CategoryRequest;
import com.sparta.bootlind.dto.requestDto.UpdateCategoryRequest;
import com.sparta.bootlind.entity.UserRoleEnum;
import com.sparta.bootlind.security.UserDetailsImpl;
import com.sparta.bootlind.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    @Operation(summary = "카테고리 목록 조회", description = "전체 카테고리 목록을 조회한다.")
    public List<String> getCategoryList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return categoryService.getCategoryList(userDetails.getUser());
    }

    @PreAuthorize(UserRoleEnum.Authority.ADMIN)
    @PostMapping("/categories")
    @Operation(summary = "카테고리 추가(관리자)", description = "카테고리를 추가한다.")
    public String addCategory(@RequestBody @Valid CategoryRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return categoryService.addCategory(request, userDetails.getUser());
    }

    @PreAuthorize(UserRoleEnum.Authority.ADMIN)
    @PutMapping("/categories")
    @Operation(summary = "카테고리 수정(관리자)", description = "카테고리를 수정한다.")
    public String updateCategory(@RequestBody @Valid UpdateCategoryRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return categoryService.updateCategory(request, userDetails.getUser());
    }

    @PreAuthorize(UserRoleEnum.Authority.ADMIN)
    @DeleteMapping("/categories")
    @Operation(summary = "카테고리 삭제(관리자)", description = "카테고리를 삭제한다.")
    public String deleteCategory(@RequestBody @Valid CategoryRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return categoryService.deleteCategory(request, userDetails.getUser());
    }
}
