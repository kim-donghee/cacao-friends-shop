package cacao.friends.shop.modules.category;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import cacao.friends.shop.modules.category.dto.CategoryDto;
import cacao.friends.shop.modules.category.dto.CategoryReturnDto;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CategoryController {
	
	private final CategoryRepository categoryRepository;
	
	private final CategoryService categoryService;
	
	private final ModelMapper modelMapper;
	
	private final String topCategoryPriffix = "/manager/topCategory";

	private final String subCategoryPriffix = "/manager/subCategory";
	
	@GetMapping("/manager/category")
	public String view() {
		return "manager/category/index";
	}
	
	@PostMapping(value = "/manager/category/update/{id}", 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody CategoryDto dto) {
		Optional<Category> byId = categoryRepository.findById(id);
		if(byId.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}
		
		if(!categoryService.isValid(dto.getName()))
			return ResponseEntity.badRequest().build();
		
		categoryService.updateCategory(byId.get(), dto.getName());
		
		return ResponseEntity.ok().build();
	}
	
	@PostMapping(value = "/manager/category/remove/{id}", 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> removeCategory(@PathVariable Long id) {
		Optional<Category> byId = categoryRepository.findById(id);
		if(byId.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}
		
		categoryService.removeCategory(byId.get());
		
		return ResponseEntity.ok().build();
	}
	
	//=== 1차 분류 카테고리 ===//
	@GetMapping(value = topCategoryPriffix + "/search",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CategoryReturnDto>> searchTopCategory() {
		List<Category> topCategorys = categoryRepository.findByParentCategoryIsNull();
		List<CategoryReturnDto>  returnDtos = 
				topCategorys.stream().map(c -> modelMapper.map(c, CategoryReturnDto.class)).collect(Collectors.toList());
		return ResponseEntity.ok(returnDtos);
	}
	
	@PostMapping(value = topCategoryPriffix + "/save", 
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CategoryReturnDto> saveTopCategory(@RequestBody CategoryDto dto) {
		if(!categoryService.isValid(dto.getName()))
			return ResponseEntity.badRequest().build();
		
		CategoryReturnDto returnDto = categoryService.createTopCategory(dto.getName());
		return ResponseEntity.ok(returnDto);
	}
	
	//=== 2차 분류 카테고리 ===//
	@GetMapping(value = subCategoryPriffix + "/search/{parentId}",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CategoryReturnDto>> searchSubCategory(@PathVariable Long parentId) {
		List<Category> subCategorys = categoryRepository.findByParentCategoryId(parentId);
		List<CategoryReturnDto>  returnDtos = 
				subCategorys.stream().map(c -> modelMapper.map(c, CategoryReturnDto.class)).collect(Collectors.toList());
		return ResponseEntity.ok(returnDtos);
	}
	
	@PostMapping(value = subCategoryPriffix + "/save/{parentId}", 
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CategoryReturnDto> saveSubCategory(@PathVariable Long parentId, @RequestBody CategoryDto dto) {
		Optional<Category> byId = categoryRepository.findById(parentId);
		if(byId.isEmpty()) 
			return ResponseEntity.badRequest().build();
		
		if(!categoryService.isValid(dto.getName()))
			return ResponseEntity.badRequest().build();
		
		CategoryReturnDto returnDto = categoryService.createSubCategory(byId.get(), dto.getName());
		return ResponseEntity.ok(returnDto);
	}
	
}
