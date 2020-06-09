package cacao.friends.shop.modules.item.search;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import lombok.Getter;

@Getter
public class ItemCondition {
	
	private String keyword;

	private Boolean published;
	
	private Boolean closed;
	
	private Boolean paused;
	
	private Long characterId;
	
	private Long categoryId;
	
	private Long subCategoryId;
	
	private Pageable pageable;
	
	private final Integer DEFAULT_SIZE = 9;
	
	public ItemCondition() { };
	
	public ItemCondition(ItemSearchForm itemSearchForm) {
		this.keyword = itemSearchForm.getKeyword();
		this.characterId = itemSearchForm.getCharacterId();
		this.categoryId = itemSearchForm.getCategoryId();
		this.subCategoryId = itemSearchForm.getSubCategoryId();
		settingItemStatus(itemSearchForm.getItemStatus());
		createPageable(itemSearchForm.getSortProperty(), itemSearchForm.getPage(), DEFAULT_SIZE);
	}
	
	public void settingItemStatus(ItemStatus status) {
		switch (status) {
		case PUBLISHED:
			this.published = true;
			this.closed = false;
			this.paused = false;
			break;
		case DRAFT:
			this.published = false;
			this.closed = false;
			this.paused = false;
			break;
		case PAUSED:
			this.published = true;
			this.closed = false;
			this.paused = true;
			break;
		case CLOSED:
			this.published = true;
			this.closed = true;
			this.paused = null;
			break;
		case ALL:
		default :
			this.published = null;
			this.closed = null;
			this.paused = null;
			break;
		}
	}
	
	private void createPageable(ItemSortProperty sortProperty, int page, int size) {
		Sort sort = Sort.unsorted();
		switch (sortProperty) {
		case NEW:
			sort = Sort.by(Order.desc("id"));
			break;
		case PRICE_ASC:
			sort = Sort.by(Order.asc("price"));
			break;
		case PRICE_DESC:
			sort = Sort.by(Order.desc("price"));
			break;
		}
		this.pageable = PageRequest.of(page, size, sort);
	}
	
}
