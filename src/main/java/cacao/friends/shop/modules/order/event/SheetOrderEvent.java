package cacao.friends.shop.modules.order.event;

import java.util.List;

import cacao.friends.shop.modules.cart.CartItem;
import cacao.friends.shop.modules.order.Orders;
import cacao.friends.shop.modules.order.form.OrderForm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SheetOrderEvent {
	
	private final Orders order;
	
	private final List<CartItem> cartItems;
	
	private final OrderForm orderForm;

}
