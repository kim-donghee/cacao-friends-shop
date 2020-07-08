package cacao.friends.shop.modules.order.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cacao.friends.shop.modules.order.OrderService;
import cacao.friends.shop.modules.order.Orders;
import cacao.friends.shop.modules.order.repository.OrdersRepository;
import cacao.friends.shop.modules.order.search.OrdersCondition;
import cacao.friends.shop.modules.order.search.OrdersSearchForm;
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
	
	@GetMapping("/{id}")
	public String orderView(@PathVariable Long id, Model model) {
		Orders order = orderRepository.findWithItemAndDeliveryById(id);
		model.addAttribute("order", order);
		return "manager/order/info";
	}
	
	@PostMapping("/comp/{id}")
	public String comp(@PathVariable Long id, RedirectAttributes attributes) {
		Orders order = orderRepository.findWithItemAndDeliveryById(id);
		orderService.comp(order);
		attributes.addFlashAttribute("message", "주문상태를 변경했습니다.");
		return "redirect:/manager/order/"+id;
	}
	
	@PostMapping("/cancel/{id}")
	public String cancel(@PathVariable Long id, RedirectAttributes attributes) {
		Orders order = orderRepository.findWithItemAndDeliveryById(id);
		orderService.cancel(order);
		attributes.addFlashAttribute("message", "주문취소를 완료했습니다.");
		return "redirect:/manager/order/"+id;
	}

}
