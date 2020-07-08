package cacao.friends.shop.modules.characterKind.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;

import cacao.friends.shop.modules.characterKind.CharacterKind;
import cacao.friends.shop.modules.characterKind.QCharacterKind;
import cacao.friends.shop.modules.item.QItem;
import cacao.friends.shop.modules.order.OrderStatus;
import cacao.friends.shop.modules.order.QOrders;
import cacao.friends.shop.modules.order.QOrdersItem;

public class CharacterKindRepositoryImpl extends QuerydslRepositorySupport implements CharacterKindRepositoryExtension {

	public CharacterKindRepositoryImpl() {
		super(CharacterKind.class);
	}

	@Override
	public List<CharacterOrderSaleDto> findLastWeekOrderSale(LocalDateTime from, LocalDateTime to) {
		QItem item = QItem.item;
		QCharacterKind character = QCharacterKind.characterKind;
		QOrders order = QOrders.orders;
		QOrdersItem orderItem = QOrdersItem.ordersItem;
		
		JPQLQuery<CharacterOrderSaleDto> query = getQuerydsl().createQuery();
		query.select(Projections.constructor(CharacterOrderSaleDto.class, character.name, order.count()))
			.from(order)
			.innerJoin(order.ordersItems, orderItem)
			.innerJoin(orderItem.item, item)
			.rightJoin(item.character, character)
			.where(order.orderStatus.eq(OrderStatus.ORDER))
			.where(order.orderedAt.between(from, to))
			.groupBy(character)
			.orderBy(character.id.asc())
		;
		return query.fetch();
	}

	@Override
	public List<CharacterOrderSaleDto> findOrderSale() {
		QItem item = QItem.item;
		QCharacterKind character = QCharacterKind.characterKind;
		QOrders order = QOrders.orders;
		QOrdersItem orderItem = QOrdersItem.ordersItem;
		
		JPQLQuery<CharacterOrderSaleDto> query = getQuerydsl().createQuery();
		query.select(
				Projections.constructor(CharacterOrderSaleDto.class, character.name, order.count()))
			.from(order)
			.innerJoin(order.ordersItems, orderItem)
			.innerJoin(orderItem.item, item)
			.rightJoin(item.character, character)
			.where(order.orderStatus.eq(OrderStatus.ORDER))
			.groupBy(character)
			.orderBy(character.id.asc())
		;	
		return query.fetch();
	}
	
}
