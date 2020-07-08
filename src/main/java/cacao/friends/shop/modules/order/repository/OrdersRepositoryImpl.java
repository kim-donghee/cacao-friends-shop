package cacao.friends.shop.modules.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;

import cacao.friends.shop.modules.delivery.QDelivery;
import cacao.friends.shop.modules.order.Orders;
import cacao.friends.shop.modules.order.QOrders;
import cacao.friends.shop.modules.order.search.OrdersCondition;

public class OrdersRepositoryImpl extends QuerydslRepositorySupport implements OrdersRepositoryExtension {

	public OrdersRepositoryImpl() {
		super(Orders.class);
	}

	@Override
	public Page<Orders> findByCondition(OrdersCondition condition, Pageable pageable) {
		QOrders order = QOrders.orders;
		QDelivery delivery = QDelivery.delivery;
		
		JPQLQuery<Orders> query = from(order);
		query.leftJoin(order.delivery, delivery).fetchJoin();
		whereCondition(query, condition, order, delivery);
		
		JPQLQuery<Orders> pageableQuery = getQuerydsl().applyPagination(pageable, query);
		QueryResults<Orders> fetchResults = pageableQuery.fetchResults();
		return new PageImpl<>(fetchResults.getResults(), pageable, fetchResults.getTotal());
	}

	@Override
	public Long countByCondition(OrdersCondition condition) {
		QOrders order = QOrders.orders;
		QDelivery delivery = QDelivery.delivery;
		
		JPQLQuery<Long> query = getQuerydsl().createQuery();
		query.select(order.count())
			.from(order)
			.leftJoin(order.delivery, delivery);
		whereCondition(query, condition, order, delivery);
		
		return query.fetchOne();
	}
	
	private void whereCondition(JPQLQuery<?> query, OrdersCondition condition, QOrders order, QDelivery delivery) {
		if(condition.getOrderStatus() != null)
			query.where(order.orderStatus.eq(condition.getOrderStatus()));
		if(condition.getName() != null) 
			query.where(delivery.name.like("%" + condition.getName() + "%"));
		if(condition.getDeliveryStatus() != null)
			query.where(delivery.status.eq(condition.getDeliveryStatus()));
	}

}
