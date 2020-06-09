package cacao.friends.shop.modules.order.search;

import cacao.friends.shop.modules.delivery.DeliveryStatus;
import cacao.friends.shop.modules.order.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrdersCondition {
	
	private String name;
	
	private OrderStatus orderStatus;
	
	private DeliveryStatus deliveryStatus;
	
	public OrdersCondition() { }
	
	public OrdersCondition(OrdersSearchForm orderSearchForm) {
		this.name = orderSearchForm.getName();
		// [READY(준비), COMP(배송), CANCEL(취소)]
		if(orderSearchForm.getStatus() != null) 
			switch (orderSearchForm.getStatus()) {
			case "READY":
				this.orderStatus = OrderStatus.ORDER;
				this.deliveryStatus = DeliveryStatus.READY;
				break;
			case "COMP":
				this.orderStatus = OrderStatus.ORDER;
				this.deliveryStatus = DeliveryStatus.COMP;
				break;
			case "CANCEL":
				this.orderStatus = OrderStatus.CANCEL;
				break;
			case "ALL":
			default:
				break;
			}
	}

}
