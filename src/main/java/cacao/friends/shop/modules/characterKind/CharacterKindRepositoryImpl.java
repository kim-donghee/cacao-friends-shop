package cacao.friends.shop.modules.characterKind;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;

import cacao.friends.shop.modules.delivery.DeliveryStatus;
import cacao.friends.shop.modules.delivery.QDelivery;
import cacao.friends.shop.modules.item.QItem;
import cacao.friends.shop.modules.order.OrderStatus;
import cacao.friends.shop.modules.order.QOrders;
import cacao.friends.shop.modules.order.QOrdersItem;

public class CharacterKindRepositoryImpl extends QuerydslRepositorySupport implements CharacterKindRepositoryExtension {

	public CharacterKindRepositoryImpl() {
		super(CharacterKind.class);
	}

	@Override
	public List<CharacterLastWeekOrderSaleDto> findLastWeekOrderSale(LocalDateTime from, LocalDateTime to) {
		QItem item = QItem.item;
		QCharacterKind character = QCharacterKind.characterKind;
		QOrders order = QOrders.orders;
		QOrdersItem orderItem = QOrdersItem.ordersItem;
		
		JPQLQuery<CharacterLastWeekOrderSaleDto> query = getQuerydsl().createQuery();
		query.select(
				Projections.constructor(CharacterLastWeekOrderSaleDto.class, character.name, order.count()))
			.from(order)
			.where(order.orderedAt.between(from, to).or(order.orderedAt.isNull()))
			.where(order.orderStatus.eq(OrderStatus.ORDER).or(order.orderStatus.isNull()))
			.innerJoin(order.ordersItems, orderItem)
			.innerJoin(orderItem.item, item)
			.rightJoin(item.character, character)
			.groupBy(character)
			.orderBy(character.id.asc())
		;	
		return query.fetch();
	}

	@Override
	public List<CharacterLastWeekOrderSaleDto> findOrderSale() {
		QItem item = QItem.item;
		QCharacterKind character = QCharacterKind.characterKind;
		QOrders order = QOrders.orders;
		QOrdersItem orderItem = QOrdersItem.ordersItem;
		
		JPQLQuery<CharacterLastWeekOrderSaleDto> query = getQuerydsl().createQuery();
		query.select(
				Projections.constructor(CharacterLastWeekOrderSaleDto.class, character.name, order.count()))
			.from(order)
			.where(order.orderStatus.eq(OrderStatus.ORDER).or(order.orderStatus.isNull()))
			.innerJoin(order.ordersItems, orderItem)
			.innerJoin(orderItem.item, item)
			.rightJoin(item.character, character)
			.groupBy(character)
			.orderBy(character.id.asc())
		;	
		return query.fetch();
	}

}
