package cacao.friends.shop.modules.account.form;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class NotificationsForm {
	
	private boolean itemCreatedByEmail;
	
	private boolean itemCreatedByWeb;
	
	@NotBlank
	private String tagName;

}
