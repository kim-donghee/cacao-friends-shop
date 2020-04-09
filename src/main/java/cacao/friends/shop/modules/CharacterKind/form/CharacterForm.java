package cacao.friends.shop.modules.tag.form;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class TagForm {
	
	@Length(min = 2, max = 20)
	private String name;
	
	@NotBlank
	private String image;

}
