package cacao.friends.shop.modules.address.form;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class AddressForm {
	
	@NotBlank
	private String city;
	
	@NotBlank
	private String street;
	
	@NotBlank
	private String zipcode;
	
	@NotBlank
	private String etc;	

}
