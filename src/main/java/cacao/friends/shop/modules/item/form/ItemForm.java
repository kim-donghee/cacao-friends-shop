package cacao.friends.shop.modules.item.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class ItemForm {
	
	@Length(min = 1, max = 20)
	private String name;
	
	@Min(0)
	private Integer price;
	
	@Min(0)
	private Integer stockQuantity;
	
	@Length(min = 1, max = 100)
	private String shortDescript;
	
	@NotBlank
	private String detail;

}
