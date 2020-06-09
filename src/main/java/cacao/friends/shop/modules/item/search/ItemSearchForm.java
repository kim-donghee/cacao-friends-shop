package cacao.friends.shop.modules.item.search;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter	
public class ItemSearchForm {
	
	private ItemSortProperty sortProperty;
	
	private ItemStatus itemStatus;
	
	private String keyword;
	
	private Long characterId;
	
	private Long categoryId;
	
	private Long subCategoryId;
	
	private int page;

}
