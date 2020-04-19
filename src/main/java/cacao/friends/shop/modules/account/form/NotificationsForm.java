package cacao.friends.shop.modules.account.form;


import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class NotificationsForm {
	
	private boolean itemCreatedByEmail;
	
	private boolean itemCreatedByWeb;
	
	@NotNull
	private Long characterId;

}
