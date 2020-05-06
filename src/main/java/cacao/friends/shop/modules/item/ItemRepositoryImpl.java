package cacao.friends.shop.modules.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;

import cacao.friends.shop.modules.category.QCategory;

public class ItemRepositoryImpl extends QuerydslRepositorySupport implements ItemRepositoryExtension{

	public ItemRepositoryImpl() {
		super(Item.class);
	}

	@Override
	public Page<Item> findByCondition(ItemCondition condition, Pageable pageable) {
		QItem item = QItem.item;
		JPQLQuery<Item> query = from(item);
		
		if(condition.getKeyword() != null)
			query.where(item.name.like("%" + condition.getKeyword() + "%"));
		if(condition.getPublished() != null)
			query.where(item.published.eq(condition.getPublished()));
		if(condition.getClosed() != null)
			query.where(item.closed.eq(condition.getClosed()));
		if(condition.getPaused() != null)
			query.where(item.paused.eq(condition.getPaused()));
		if(condition.getCharacterId() != null) 
			query.where(item.character.id.eq(condition.getCharacterId()));
		if(condition.getCategoryId() != null) {
			QItemCategory itemCategory = QItemCategory.itemCategory;
			QCategory category = QCategory.category;
			query.leftJoin(item.itemCategorys, itemCategory);
			query.leftJoin(itemCategory.category, category);
			
			query.distinct();
			
			query.where(category.parentCategory.id.eq(condition.getCategoryId()));
			if(condition.getSubCategoryId() != null) 
				query.where(category.id.eq(condition.getSubCategoryId()));
		}
		
		JPQLQuery<Item> pageableQuery = getQuerydsl().applyPagination(pageable, query);
		QueryResults<Item> fetchResults = pageableQuery.fetchResults();
		return new PageImpl<>(fetchResults.getResults(), pageable, fetchResults.getTotal());
	}

}
