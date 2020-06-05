package cacao.friends.shop.modules.item.form;

import cacao.friends.shop.modules.item.search.ItemSortProperty;
import cacao.friends.shop.modules.item.search.ItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ItemSearchForm {
	
	private ItemSortProperty sortProperty;
	
	private ItemStatus itemStatus;
	
	private String keyword;
	
	private Long characterId;
	
	private Long categoryId;
	
	private Long subCategoryId;
	
	private int page;

}
