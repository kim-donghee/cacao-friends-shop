package cacao.friends.shop.modules.order;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cacao.friends.shop.modules.cart.Cart;
import cacao.friends.shop.modules.cart.CartRepository;
import cacao.friends.shop.modules.cart.form.CartSearchForm;
import cacao.friends.shop.modules.item.Item;
import cacao.friends.shop.modules.item.ItemRepository;
import cacao.friends.shop.modules.member.CurrentMember;
import cacao.friends.shop.modules.member.Member;
import cacao.friends.shop.modules.order.form.OrderForm;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
@SessionAttributes( {"cartSearchForm"} )
public class OrdersController {
	
	private final OrdersRepository ordersRepository;
	
	private final OrderService orderService;
	
	private final CartRepository cartRepository;
	
	private final ItemRepository itemRepository;
	
	@GetMapping
	public String ordersView(@CurrentMember Member member, Model model) {
		model.addAttribute(member);
		model.addAttribute(ordersRepository.findWithItemByMember(member));
		return "member/order/list";
	}
	
	@PostMapping("/sheet")
	public String orderSheet(@CurrentMember Member member, CartSearchForm cartSearchForm, Model model) {
		model.addAttribute(member);
		model.addAttribute(new OrderForm());
		model.addAttribute("cartList", cartRepository.findAllById(cartSearchForm.getId()));
		return "member/order/sheet";
	}
	
	@PostMapping("/sheet/process")
	public String orderProcess(@CurrentMember Member member, CartSearchForm cartSearchForm, 
			@Valid OrderForm orderForm, BindingResult result, 
			SessionStatus sessionStatus, RedirectAttributes attributes, Model model) {
		List<Cart> cartList = cartRepository.findAllById(cartSearchForm.getId());
		if(result.hasErrors()) {
			model.addAttribute(member);
			model.addAttribute("cartList", cartList);
			return "member/order/sheet";
		}
		orderService.order(member, cartList, orderForm);
		sessionStatus.setComplete();
		return "redirect:/";
	}
	
	@PostMapping("/directsheet/{itemId}")
	public String orderDirectsheet(@CurrentMember Member member, @PathVariable Long itemId, Integer quantity, Model model) {
		model.addAttribute(member);
		model.addAttribute("quantity", quantity);
		model.addAttribute(new OrderForm());
		model.addAttribute(itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("해당하는 상품이 없습니다.")));
		return "member/order/directsheet";
	}
	
	@PostMapping("/directsheet/{itemId}/process")
	public String orderDirectsheetProcess(@CurrentMember Member member, @PathVariable Long itemId, Integer quantity,
			@Valid OrderForm orderForm, BindingResult result, RedirectAttributes attributes, Model model) {
		Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("해당하는 상품이 없습니다."));
		if(result.hasErrors()) {
			model.addAttribute(member);
			model.addAttribute("quantity", quantity);
			model.addAttribute(item);
			return "member/order/directsheet";
		}
		orderService.directOrder(member, item, orderForm, quantity);
		return "redirect:/";
	}

}
