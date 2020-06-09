package cacao.friends.shop.modules.order.event;

import cacao.friends.shop.modules.item.Item;
import cacao.friends.shop.modules.order.Orders;
import cacao.friends.shop.modules.order.form.OrderForm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DirectSheetOrderEvent {
	
	private final Orders order;
	
	private final Item item;
	
	private Integer quantity;	
	
	private final OrderForm orderForm;

}
