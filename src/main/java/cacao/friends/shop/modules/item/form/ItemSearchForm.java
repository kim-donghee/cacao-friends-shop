package cacao.friends.shop.modules.item.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ItemSearchForm {
	
	private String sortProperty;
	
	private String itemSatus;
	
	private String keyword;
	
	private Long characterId;
	
	private Long categoryId;
	
	private Long subCategoryId;

}
