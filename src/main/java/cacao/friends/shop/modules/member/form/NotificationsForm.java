package cacao.friends.shop.modules.member.form;


import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class NotificationsForm {
	
	private boolean itemCreatedByEmail;
	
	private boolean itemCreatedByWeb;
	
	@NotNull
	private Long characterId;

}
