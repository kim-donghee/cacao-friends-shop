package cacao.friends.shop.modules.item.search;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

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
	
	private Pageable pageable;
	
	public static ItemCondition createCondition(ItemSearchForm form) {
		ItemCondition condition = new ItemCondition();
		condition.setKeyword(form.getKeyword());
		condition.setCharacterId(form.getCharacterId());
		condition.setCategoryId(form.getCategoryId());
		condition.setSubCategoryId(form.getSubCategoryId());
		condition.settingItemStatus(form.getItemStatus());
		condition.setPageable(condition.createPageable(form.getSortProperty(), form.getPage(), 9));
		return condition;
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
	
	private Pageable createPageable(ItemSortProperty sortProperty, int page, int size) {
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
		return PageRequest.of(page, size, sort);
	}
	
}
