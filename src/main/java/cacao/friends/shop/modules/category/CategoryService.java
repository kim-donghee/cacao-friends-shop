package cacao.friends.shop.modules.category;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.modules.category.dto.CategoryReturnDto;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
	
	private final CategoryRepository repo;
	
	private final ModelMapper modelMapper;
	
	public CategoryReturnDto createTopCategory(String name) {
		Category saveCategory = repo.save(Category.builder().name(name).build());
		return modelMapper.map(saveCategory, CategoryReturnDto.class);
	}
	
	public CategoryReturnDto createSubCategory(Category parentCategory, String name) {
		Category saveCategory = repo.save(Category.builder().parentCategory(parentCategory).name(name).build());
		return modelMapper.map(saveCategory, CategoryReturnDto.class);
	}
	
	public void updateCategory(Category category, String name) {
		category.setName(name);
	}
	
	public void removeCategory(Category category) {
		repo.delete(category);
	}
	
	public List<CategoryReturnDto> topCategories() {
		return toDto(repo.findByParentCategoryIsNull());
	}
	
	public List<CategoryReturnDto> subCategories(Long parentId) {
		return toDto(repo.findByParentCategoryId(parentId));
	}
	
	private List<CategoryReturnDto> toDto(List<Category> categories) {
		List<CategoryReturnDto> returnDtoList = 
				categories.stream().map(c -> modelMapper.map(c, CategoryReturnDto.class)).collect(Collectors.toList());
		return returnDtoList;
	}

}
