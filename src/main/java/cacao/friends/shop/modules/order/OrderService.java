package cacao.friends.shop.modules.order;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.modules.cart.CartItem;
import cacao.friends.shop.modules.item.Item;
import cacao.friends.shop.modules.member.Member;
import cacao.friends.shop.modules.order.event.DirectSheetOrderEvent;
import cacao.friends.shop.modules.order.event.SheetOrderEvent;
import cacao.friends.shop.modules.order.form.OrderForm;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
	
	private final OrdersRepository ordersRepository;
	
	private final ApplicationEventPublisher eventPublisher;
	
	// 주문
	public void order(Member member, List<CartItem> cartItems, OrderForm form) {
		Orders saveOrder = ordersRepository.save(Orders.builder().member(member).build());
		saveOrder.order();
		eventPublisher.publishEvent(new SheetOrderEvent(saveOrder, cartItems, form));
	}

	// 바로주문
	public void directOrder(Member member, Item item, OrderForm form, int quantity) {
		Orders saveOrder = ordersRepository.save(Orders.builder().member(member).build());
		saveOrder.order();
		eventPublisher.publishEvent(new DirectSheetOrderEvent(saveOrder, item, form));
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

}
