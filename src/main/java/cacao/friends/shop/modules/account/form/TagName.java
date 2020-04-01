package cacao.friends.shop.modules.account.form;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class TagName {
	
	@NotBlank
	private String tagName;

}
