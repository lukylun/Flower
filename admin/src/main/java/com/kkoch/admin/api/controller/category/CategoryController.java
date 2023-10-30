package com.kkoch.admin.api.controller.category;

import com.kkoch.admin.api.ApiResponse;
import com.kkoch.admin.api.controller.category.request.AddCategoryRequest;
import com.kkoch.admin.api.controller.category.request.SetCategoryRequest;
import com.kkoch.admin.api.controller.category.response.CategoryForMemberResponse;
import com.kkoch.admin.api.controller.category.response.CategoryResponse;
import com.kkoch.admin.api.service.category.CategoryQueryService;
import com.kkoch.admin.api.service.category.CategoryService;
import com.kkoch.admin.api.service.category.dto.AddCategoryDto;
import com.kkoch.admin.api.service.category.dto.SetCategoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/admin-service/categories")
@RestController
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryQueryService categoryQueryService;

    @PostMapping
    public ApiResponse<Long> addCategory(@Valid @RequestBody AddCategoryRequest request) {
        log.info("<카테고리 등록> Controller");
        AddCategoryDto dto = request.toAddCategoryDto();
        Long result = categoryService.addCategory(dto);

        return ApiResponse.ok(result);
    }

    @GetMapping("/type")
    public ApiResponse<List<String>> getTypes(
            @RequestParam(defaultValue = "절화") String code
    ) {
        log.info("<카테고리 품목 조회> Controller");
        List<String> types = categoryQueryService.getTypesForMember(code);
        return ApiResponse.ok(types);
    }

    @GetMapping("/name")
    public ApiResponse<List<String>> getNames(
            @RequestParam(defaultValue = "절화") String code,
            @RequestParam(defaultValue = "") String type
            //날짜
    ) {
        log.info("<카테고리 품종 조회> Controller");
        List<String> names = categoryQueryService.getNamesForMember(code, type);
        return ApiResponse.ok(names);
    }

    @GetMapping("/{parentId}")
    public ApiResponse<List<CategoryResponse>> getCategoriesParentId(@PathVariable Long parentId) {
        log.info("<카테고리 하위 카테고리 조회> Controller");
        List<CategoryResponse> subCategories = categoryQueryService.getCategoriesByParentId(parentId);
        return ApiResponse.ok(subCategories);
    }

    @PatchMapping("/{categoryId}")
    public ApiResponse<CategoryResponse> setCategory(@PathVariable Long categoryId
            , @Valid @RequestBody SetCategoryRequest request) {
        log.info("<카테고리 수정> Controller");
        SetCategoryDto dto = request.toSetCategoryDto();
        CategoryResponse result = categoryService.setCategory(categoryId, dto);
        return ApiResponse.of(HttpStatus.MOVED_PERMANENTLY, "카테고리가 수정되었습니다.", result);
    }

    @DeleteMapping("/{categoryId}")
    public ApiResponse<Long> removeCategory(@PathVariable Long categoryId) {
        log.info("<카테고리 삭제> Controller");
        Long removeId = categoryService.removeCategory(categoryId);
        return ApiResponse.of(HttpStatus.MOVED_PERMANENTLY, "카테고리가 삭제되었습니다.", removeId);

    }

    //    @GetMapping
    public ApiResponse<List<CategoryForMemberResponse>> getCategories(
            @RequestParam String code
    ) {
        List<CategoryForMemberResponse> categories = categoryQueryService.getCategories(code);
        return ApiResponse.ok(categories);
    }
}
