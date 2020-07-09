package cacao.friends.shop.modules.category;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import cacao.friends.shop.modules.category.dto.CategoryDto;
import cacao.friends.shop.modules.category.dto.CategoryReturnDto;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manager/category")
public class CategoryControllerManager {
	
	private final CategoryService categoryService;
	
	@GetMapping
	public String view() {
		return "manager/category/index";
	}
	
	@PostMapping(value = "/update/{id}", 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody @Valid CategoryDto dto, Errors errors) {
		if(errors.hasErrors()) 
			throw new IllegalArgumentException();
		categoryService.updateCategory(id, dto);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping(value = "/remove/{id}", 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> removeCategory(@PathVariable Long id) {
		categoryService.removeCategory(id);
		return ResponseEntity.ok().build();
	}
	
	//=== 1차 분류 카테고리 ===//
	@GetMapping(value = "/top/search",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CategoryReturnDto>> searchTopCategory() {
		return ResponseEntity.ok(categoryService.topCategories());
	}
	
	@PostMapping(value = "/top/save", 
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CategoryReturnDto> saveTopCategory(@RequestBody @Valid CategoryDto dto, Errors errors) {
		if(errors.hasErrors()) 
			throw new IllegalArgumentException();
		return ResponseEntity.ok(categoryService.createTopCategory(dto));
	}
	
	//=== 2차 분류 카테고리 ===//
	@GetMapping(value = "/sub/search/{parentId}",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CategoryReturnDto>> searchSubCategory(@PathVariable Long parentId) {
		return ResponseEntity.ok(categoryService.subCategories(parentId));
	}
	
	@PostMapping(value = "/sub/save/{parentId}", 
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CategoryReturnDto> saveSubCategory(@PathVariable Long parentId, 
			@RequestBody @Valid CategoryDto dto, Errors errors) {
		if(errors.hasErrors()) 
			throw new IllegalArgumentException();
		return ResponseEntity.ok(categoryService.createSubCategory(parentId, dto));
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	private ResponseEntity<?> handlerIllegalArgumentException(IllegalArgumentException e) {
		return ResponseEntity.badRequest().build();
	}
	
}
