package cacao.friends.shop.modules.category.dto;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class CategoryDto {
	
	@Length(min = 2, max = 50)
	private String name;

}
