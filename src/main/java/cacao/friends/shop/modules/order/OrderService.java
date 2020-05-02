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
	
	public void order(Member member, List<Cart> carts, OrderForm form) {
		Orders saveOrder = ordersRepository.save(Orders.builder().member(member).build());

		Delivery delivery = new Delivery();
		delivery.updateAddress(form.getAddress());
		modelMapper.map(form, delivery);
		delivery.setStatus(DeliveryStatus.COMP);
		saveOrder.updateDelivery(delivery);
		em.persist(delivery);
		
		carts.forEach(c -> {
			OrdersItem orderItem = OrdersItem.builder()
				.item(c.getItem())
				.quantity(c.getQuantity())
				.itemName(c.getItemName())
				.price(c.getItemPrice())
				.build();
			saveOrder.addItem(orderItem);
			em.persist(orderItem);
		});

		saveOrder.order();
		cartService.remove(carts);
	}

	public void directOrder(Member member, Item item, OrderForm form, int quantity) {
		Orders saveOrder = ordersRepository.save(Orders.builder().member(member).build());
		Delivery delivery = new Delivery();
		OrdersItem orderItem = OrdersItem.builder()
				.item(item)
				.quantity(quantity)
				.itemName(item.getName())
				.price(item.getPrice())
				.build();
		saveOrder.addItem(orderItem);
		em.persist(orderItem);
		
		modelMapper.map(form, delivery);
		delivery.setStatus(DeliveryStatus.COMP);
		saveOrder.updateDelivery(delivery);
		em.persist(delivery);
		
		saveOrder.order();
	}
	
	public void cancel(Orders order) {
		order.cancel();
	}

}
