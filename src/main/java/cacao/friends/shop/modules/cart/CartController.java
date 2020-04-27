package cacao.friends.shop.modules.cart;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CartController {
	
	private final CartService cartService;
	
	@GetMapping("/cart")
	public String cartView(Model model) {
		model.addAttribute(cartService.findCart());
		return "member/cart";
	}
	
	@PostMapping("/cart/add/{id}")
	public String addCart(@PathVariable Long id, RedirectAttributes attributes) {
		cartService.addCart(id, 1);
		attributes.addFlashAttribute("message", "카트에 추가했습니다.");
		return "redirect:/item/" + id;
	}
	
	@PostMapping("/cart/remove/{id}")
	public String removeCart(@PathVariable Long id, RedirectAttributes attributes) {
		cartService.removeCart(id);
		attributes.addFlashAttribute("message", "카트에서 삭제했습니다.");
		return "redirect:/cart";
	}
	
	@PostMapping("/cart/edit/{id}")
	public String editCart(@PathVariable Long id, int quantity, RedirectAttributes attributes) {
		cartService.addCart(id, quantity);
		attributes.addFlashAttribute("message", "카트를 수정했습니다.");
		return "redirect:/cart";
	}
}
