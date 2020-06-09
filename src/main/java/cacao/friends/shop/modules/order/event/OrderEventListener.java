package cacao.friends.shop.modules.order.event;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.modules.cart.CartItem;
import cacao.friends.shop.modules.cart.CartService;
import cacao.friends.shop.modules.delivery.Delivery;
import cacao.friends.shop.modules.delivery.DeliveryStatus;
import cacao.friends.shop.modules.item.Item;
import cacao.friends.shop.modules.order.Orders;
import cacao.friends.shop.modules.order.OrdersItem;
import cacao.friends.shop.modules.order.form.OrderForm;
import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class OrderEventListener {
	
	private final ModelMapper modelMapper;
	
	private final CartService cartService;
	
	@EventListener
	public void handlerOrderEvent(SheetOrderEvent event) {
		Orders order = event.getOrder();
		OrderForm orderForm = event.getOrderForm();
		List<CartItem> cartItems = event.getCartItems();
		
		for(CartItem ci : cartItems) {
			OrdersItem ordersItem = new OrdersItem(ci.getItem(), ci.getQuantity());
			order.addItem(ordersItem);
		}
		
		Delivery delivery = modelMapper.map(orderForm, Delivery.class);
		delivery.updateAddress(orderForm.getAddress());
		delivery.setStatus(DeliveryStatus.READY);
		order.updateDelivery(delivery);
		
		cartService.removeItem(cartItems);
	}
	
	@EventListener
	public void handlerOrderEvent(DirectSheetOrderEvent event) {
		Orders order = event.getOrder();
		OrderForm orderForm = event.getOrderForm();
		Item item = event.getItem();
		int quantity = event.getQuantity();
		
		OrdersItem ordersItem = new OrdersItem(item, quantity);
		order.addItem(ordersItem);
		
		Delivery delivery = modelMapper.map(orderForm, Delivery.class);
		delivery.updateAddress(orderForm.getAddress());
		delivery.setStatus(DeliveryStatus.READY);
		order.updateDelivery(delivery);
	}

}
