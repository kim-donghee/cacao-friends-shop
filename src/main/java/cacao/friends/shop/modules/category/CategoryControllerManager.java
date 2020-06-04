package cacao.friends.shop.modules.category;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
	
	private final CategoryRepository categoryRepository;
	
	private final CategoryService categoryService;
	
	private final ModelMapper modelMapper;
	
	@GetMapping
	public String view() {
		return "manager/category/index";
	}
	
	@PostMapping(value = "/update/{id}", 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody CategoryDto dto) {
		Category findCategory = categoryRepository.findById(id).get();
		if(findCategory == null) {
			return ResponseEntity.badRequest().build();
		}
		
		if(!categoryService.isValid(dto.getName()))
			return ResponseEntity.badRequest().build();
		
		categoryService.updateCategory(findCategory, dto.getName());
		
		return ResponseEntity.ok().build();
	}
	
	@PostMapping(value = "/remove/{id}", 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> removeCategory(@PathVariable Long id) {
		Category findCategory = categoryRepository.findById(id).get();
		if(findCategory == null) {
			return ResponseEntity.badRequest().build();
		}
		
		categoryService.removeCategory(findCategory);
		
		return ResponseEntity.ok().build();
	}
	
	//=== 1차 분류 카테고리 ===//
	@GetMapping(value = "/top/search",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CategoryReturnDto>> searchTopCategory() {
		List<Category> topCategories = categoryRepository.findByParentCategoryIsNull();
		List<CategoryReturnDto>  returnDtoList =
				topCategories.stream().map(c -> modelMapper.map(c, CategoryReturnDto.class)).collect(Collectors.toList());
		return ResponseEntity.ok(returnDtoList);
	}
	
	@PostMapping(value = "/top/save", 
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CategoryReturnDto> saveTopCategory(@RequestBody CategoryDto dto) {
		if(!categoryService.isValid(dto.getName()))
			return ResponseEntity.badRequest().build();
		
		CategoryReturnDto returnDto = categoryService.createTopCategory(dto.getName());
		return ResponseEntity.ok(returnDto);
	}
	
	//=== 2차 분류 카테고리 ===//
	@GetMapping(value = "/sub/search/{parentId}",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CategoryReturnDto>> searchSubCategory(@PathVariable Long parentId) {
		List<Category> subCategories = categoryRepository.findByParentCategoryId(parentId);
		List<CategoryReturnDto>  returnDtoList = 
				subCategories.stream().map(c -> modelMapper.map(c, CategoryReturnDto.class)).collect(Collectors.toList());
		return ResponseEntity.ok(returnDtoList);
	}
	
	@PostMapping(value = "/sub/save/{parentId}", 
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CategoryReturnDto> saveSubCategory(@PathVariable Long parentId, @RequestBody CategoryDto dto) {
		Category parentCategory = categoryRepository.findById(parentId).get();
		if(parentCategory == null) {
			return ResponseEntity.badRequest().build();
		}
		
		if(!categoryService.isValid(dto.getName()))
			return ResponseEntity.badRequest().build();
		
		CategoryReturnDto returnDto = categoryService.createSubCategory(parentCategory, dto.getName());
		return ResponseEntity.ok(returnDto);
	}
	
}
