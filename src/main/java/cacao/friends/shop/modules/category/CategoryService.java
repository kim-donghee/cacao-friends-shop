package cacao.friends.shop.modules.category;

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
	
	public boolean isValid(String name) {
		int nameLength = name.length();
		
		if(name == null || nameLength < 2 || nameLength > 50)
			return false;
		
		return true;
	}

}
