package com.kkoch.admin.api.service.category;

import com.kkoch.admin.api.controller.category.response.CategoryForMemberResponse;
import com.kkoch.admin.api.controller.category.response.CategoryResponse;
import com.kkoch.admin.domain.plant.Category;
import com.kkoch.admin.domain.plant.repository.CategoryQueryRepository;
import com.kkoch.admin.domain.plant.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@CacheConfig(cacheNames = {"categoryCache"})
@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class CategoryQueryService {

    private final CategoryQueryRepository categoryQueryRepository;

    @Cacheable(value = "categoryCache")
    public List<String> getTypesForMember(String code) {
        return categoryQueryRepository.getTypes(code);
    }

    @Cacheable(value = "categoryCache")
    public List<String> getNamesForMember(String code, String type) {
        return categoryQueryRepository.getNames(code, type);
    }

    @Cacheable(value = "categoryCache")
    public List<CategoryForMemberResponse> getCategories(String parentName) {
        List<Category> categories = categoryQueryRepository.getCategories(parentName);

        return categories.stream()
                .map(CategoryForMemberResponse::of)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "categoryCache")
    public List<CategoryResponse> getCategoriesByParentId(Long parentId) {
        return categoryQueryRepository.getCategoriesByParentId(parentId)
                .stream().map(CategoryResponse::new).collect(Collectors.toList());

    }
}
