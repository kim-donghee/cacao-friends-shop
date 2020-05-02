package cacao.friends.shop.modules.order.form;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import cacao.friends.shop.modules.address.form.AddressForm;
import lombok.Data;

@Data
public class OrderForm {
	
	@NotBlank
	private String name;
	
	@NotBlank
	private String tel;
	
	private String requestDetails;
	
	@Valid
	private AddressForm address;

}
