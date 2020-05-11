package cacao.friends.shop.modules.item;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemCondition {
	
	private String keyword;

	private Boolean published;
	
	private Boolean closed;
	
	private Boolean paused;
	
	private Long characterId;
	
	private Long categoryId;
	
	private Long subCategoryId;
	
	public void settingItemStatus(String itemStatus) {
		switch (itemStatus) {
		case "PUBLISHED":
			this.published = true;
			this.closed = false;
			this.paused = false;
			break;
		case "DRAFT":
			this.published = false;
			this.closed = false;
			this.paused = false;
			break;
		case "PAUSED":
			this.published = true;
			this.closed = false;
			this.paused = true;
			break;
		case "CLOSED":
			this.published = true;
			this.closed = true;
			this.paused = null;
			break;
		case "ALL":
		default :
			this.published = null;
			this.closed = null;
			this.paused = null;
			break;
		}
	}
	
}
