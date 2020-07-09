package cacao.friends.shop.modules.category;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.modules.category.dto.CategoryDto;
import cacao.friends.shop.modules.category.dto.CategoryReturnDto;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
	
	private final CategoryRepository repo;
	
	private final ModelMapper modelMapper;
	
	public CategoryReturnDto createTopCategory(CategoryDto dto) {
		Category saveCategory = repo.save(modelMapper.map(dto, Category.class));
		return modelMapper.map(saveCategory, CategoryReturnDto.class);
	}
	
	public CategoryReturnDto createSubCategory(Long parentId, CategoryDto dto) {
		Category parentCategory = findById(parentId);
		Category saveCategory = repo.save(
				Category.builder().parentCategory(parentCategory).name(dto.getName()).build());
		return modelMapper.map(saveCategory, CategoryReturnDto.class);
	}
	
	public void updateCategory(Long id, CategoryDto dto) {
		Category category = findById(id);
		modelMapper.map(dto, category);		
	}
	
	public void removeCategory(Long id) {
		Category category = findById(id);
		repo.delete(category);
	}
	
	public List<CategoryReturnDto> topCategories() {
		return toDto(repo.findByParentCategoryIsNull());
	}
	
	public List<CategoryReturnDto> subCategories(Long parentId) {
		return toDto(repo.findByParentCategoryId(parentId));
	}
	
	private Category findById(Long id) {
		return repo.findById(id).orElseThrow(() 
				-> new IllegalArgumentException("해당하는 카테고리가 존재하지 않습니다."));
	}
	
	private List<CategoryReturnDto> toDto(List<Category> categories) {
		List<CategoryReturnDto> returnDtoList = 
				categories.stream().map(c -> modelMapper.map(c, CategoryReturnDto.class)).collect(Collectors.toList());
		return returnDtoList;
	}

}
