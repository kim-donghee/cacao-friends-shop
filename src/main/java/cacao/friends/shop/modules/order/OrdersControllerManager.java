package cacao.friends.shop.modules.order;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cacao.friends.shop.modules.order.form.OrdersSearchForm;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/manager/order")
@RequiredArgsConstructor
public class OrdersControllerManager {
	
	private final OrdersRepository orderRepository;
	
	private final OrderService orderService;
	
	@GetMapping
	public String ordersView(OrdersSearchForm orderSearchForm, 
			@PageableDefault(size = 20, page = 0, direction = Direction.DESC, sort = "orderedAt") 
			Pageable pageable, Model model) {
		OrdersCondition condition = new OrdersCondition(orderSearchForm);
		model.addAttribute("name", orderSearchForm.getName());
		model.addAttribute("status", orderSearchForm.getStatus());
		model.addAttribute("orderPage", orderRepository.findByCondition(condition, pageable));
		return "manager/order/list";
	}

}
