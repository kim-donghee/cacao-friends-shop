package cacao.friends.shop.modules.order;

import java.util.List;

import javax.persistence.EntityManager;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.modules.cart.Cart;
import cacao.friends.shop.modules.cart.CartService;
import cacao.friends.shop.modules.delivery.Delivery;
import cacao.friends.shop.modules.delivery.DeliveryStatus;
import cacao.friends.shop.modules.item.Item;
import cacao.friends.shop.modules.member.Member;
import cacao.friends.shop.modules.order.form.OrderForm;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
	
	private final EntityManager em;
	
	private final OrdersRepository ordersRepository;
	
	private final CartService cartService;
	
	private final ModelMapper modelMapper;
	
	// 주문
	public void order(Member member, List<Cart> carts, OrderForm form) {
		Orders saveOrder = ordersRepository.save(Orders.builder().member(member).build());
		Delivery delivery = createDelivery(saveOrder, form);
		em.persist(delivery);
		carts.forEach(c -> {
			OrdersItem orderItem = createOrdersItem(c.getItem(), c.getQuantity());
			saveOrder.addItem(orderItem);
			em.persist(orderItem);
		});
		saveOrder.order();
		cartService.remove(carts);
	}

	// 바로주문
	public void directOrder(Member member, Item item, OrderForm form, int quantity) {
		Orders saveOrder = ordersRepository.save(Orders.builder().member(member).build());
		Delivery delivery = createDelivery(saveOrder, form);
		em.persist(delivery);
		OrdersItem orderItem = createOrdersItem(item, quantity);
		saveOrder.addItem(orderItem);
		em.persist(orderItem);
		saveOrder.order();
	}
	
	// 주문고객이 주문 취소
	public void cancel(Orders order, Member currentMember) {
		if(!order.memberEq(currentMember)) 
			throw new RuntimeException("주문 취소 권한이 없습니다.");
		order.cancel();
	}
	
	// 관리자가 주문 취소
	public void cancel(Orders order) {
		order.cancel();
	}
	
	// 배송준비 -> 배송중
	public void comp(Orders order) {
		order.comp();
	}
	
	private OrdersItem createOrdersItem(Item item, int quantity) {
		return OrdersItem.builder()
				.item(item)
				.quantity(quantity)
				.itemName(item.getName())
				.price(item.getPrice())
				.build();
	}
	
	private Delivery createDelivery(Orders order, OrderForm form) {
		Delivery delivery = modelMapper.map(form, Delivery.class);
		delivery.updateAddress(form.getAddress());
		delivery.setStatus(DeliveryStatus.READY);
		order.updateDelivery(delivery);
		return delivery;
	}

}
