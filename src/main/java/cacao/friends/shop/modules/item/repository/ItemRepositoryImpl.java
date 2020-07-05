package cacao.friends.shop.modules.item.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;

import cacao.friends.shop.modules.category.QCategory;
import cacao.friends.shop.modules.characterKind.QCharacterKind;
import cacao.friends.shop.modules.delivery.QDelivery;
import cacao.friends.shop.modules.item.Item;
import cacao.friends.shop.modules.item.QItem;
import cacao.friends.shop.modules.item.QItemCategory;
import cacao.friends.shop.modules.item.search.ItemCondition;
import cacao.friends.shop.modules.member.Member;
import cacao.friends.shop.modules.member.QMember;
import cacao.friends.shop.modules.order.QOrders;
import cacao.friends.shop.modules.order.QOrdersItem;

public class ItemRepositoryImpl extends QuerydslRepositorySupport implements ItemRepositoryExtension{

	public ItemRepositoryImpl() {
		super(Item.class);
	}

	@Override
	public Page<Item> findByCondition(ItemCondition condition) {
		Pageable pageable = condition.getPageable();
		QItem item = QItem.item;
		JPQLQuery<Item> query = from(item);
		whereCondition(query, condition, item);
		JPQLQuery<Item> pageableQuery = getQuerydsl().applyPagination(pageable, query);
		QueryResults<Item> fetchResults = pageableQuery.fetchResults();
		return new PageImpl<>(fetchResults.getResults(), pageable, fetchResults.getTotal());
	}
	
	@Override
	public Long countByCondition(ItemCondition condition) {
		QItem item = QItem.item;
		JPQLQuery<Long> query = getQuerydsl().createQuery();
		query.select(item.count()).from(item);
		whereCondition(query, condition, item);
		return query.fetchOne();
	}
	
	private void whereCondition(JPQLQuery<?> query, ItemCondition condition, QItem item) {
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
	}

	@Override
	public List<Item> findPopular() {
		QItem item = QItem.item;
		QOrders order = QOrders.orders;
		QOrdersItem orderItem = QOrdersItem.ordersItem;
		QDelivery delivery = QDelivery.delivery;
		
		JPQLQuery<Long> subQuery = getQuerydsl().createQuery();
		subQuery.select(item.id)
			.from(order)
			.leftJoin(order.ordersItems, orderItem)
			.leftJoin(orderItem.item, item)
			.leftJoin(order.delivery, delivery)
			.where(item.published.eq(true))
			.where(item.closed.eq(false))
			.where(item.paused.eq(false))
			.groupBy(order, orderItem, delivery, item)
			.orderBy(item.id.count().desc(), item.publishedDateTime.desc())
			.limit(6)
		;
		
		JPQLQuery<Item> query = from(item).where(item.id.in(subQuery));
		
		return query.fetch();
	}

	@Override
	public List<Item> findByMemberPick(Member currentMember) {
		QItem item = QItem.item;
		QCharacterKind character = QCharacterKind.characterKind;
		QMember member = QMember.member;
		
		JPQLQuery<Long> subQuery = getQuerydsl().createQuery();
		subQuery.select(character.id)
			.from(member)
			.innerJoin(member.pickCharacter, character)
			.where(member.username.eq(currentMember.getUsername()));
		
		JPQLQuery<Item> query = from(item)
				.leftJoin(item.character, character).fetchJoin()
				.where(item.published.eq(true))
				.where(item.closed.eq(false))
				.where(item.paused.eq(false))
				.where(character.id.in(subQuery))
				.orderBy(item.publishedDateTime.desc())
				.limit(6);
		
		return query.fetch();
	}
	
}
