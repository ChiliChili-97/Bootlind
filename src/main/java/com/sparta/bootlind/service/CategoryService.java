package com.sparta.bootlind.service;

import com.sparta.bootlind.entity.Category;
import com.sparta.bootlind.entity.User;
import com.sparta.bootlind.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;


    public List<String> getCategoryList() {
        List<Category> categories = categoryRepository.findAll();

        List<String> categoryList = new ArrayList<>();
        for(Category category : categories)
            categoryList.add(category.getCategory());
        return categoryList;
    }

    public String addCategory(String categoryname, User user) {
        if(categoryRepository.findByCategory(categoryname).isPresent())
            throw new IllegalArgumentException("해당 카테고리가 존재합니다.");

       Category category =  categoryRepository.save(new Category(categoryname));
       return categoryname + " 가 추가되었습니다";
    }

    @Transactional
    public String updateCategory(String categoryname, String newcategoryname, User user) {
        Category category =(Category) categoryRepository.findByCategory(categoryname).orElseThrow(
                ()-> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다.")
        );
        category.update(newcategoryname);
        return categoryname + "에서 " + newcategoryname + " 으로 변경되었습니다.";
    }


    public String deleteCategory(String categoryname, User user) {
        Category category =(Category) categoryRepository.findByCategory(categoryname).orElseThrow(
                ()-> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다.")
        );
        categoryRepository.deleteById(category.getId());
        return categoryname + " 이 삭제되었습니다.";
    }
}
