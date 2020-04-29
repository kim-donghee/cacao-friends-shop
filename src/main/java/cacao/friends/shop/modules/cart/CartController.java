package cacao.friends.shop.modules.cart;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cacao.friends.shop.modules.cart.form.CartEditForm;
import cacao.friends.shop.modules.cart.form.CartSearchForm;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CartController {
	
	private final CartService cartService;
	
	@GetMapping("/cart")
	public String cartView(Model model) {
		model.addAttribute("cartList", cartService.findAll());
		return "member/cart";
	}
	
	@PostMapping("/cart/add/{id}")
	public String addCart(@PathVariable Long id, RedirectAttributes attributes) {
		cartService.add(id, "1");
		attributes.addFlashAttribute("message", "카트에 추가했습니다.");
		return "redirect:/item/" + id;
	}
	
	@PostMapping("/cart/remove/{id}")
	public String removeCart(@PathVariable Long id) {
		cartService.remove(id);
		return "redirect:/cart";
	}
	
	@PostMapping("/cart/remove")
	public String remove(CartSearchForm form) {
		cartService.remove(form.getItemId());
		return "redirect:/cart";
	}
	
	@PostMapping("/cart/remove/all")
	public String removeAll(CartSearchForm form) {
		cartService.removeAll();
		return "redirect:/cart";
	}
	
	@PostMapping(value = "/cart/edit/{id}", 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> editCart(@PathVariable Long id, @RequestBody CartEditForm cartEditForm) {
		cartService.add(id, cartEditForm.getQuantity());
		return ResponseEntity.ok().build();
	}
}
