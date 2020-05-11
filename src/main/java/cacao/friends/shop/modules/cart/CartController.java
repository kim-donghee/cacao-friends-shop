package cacao.friends.shop.modules.cart;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cacao.friends.shop.modules.cart.form.CartForm;
import cacao.friends.shop.modules.cart.form.CartSearchForm;
import cacao.friends.shop.modules.item.Item;
import cacao.friends.shop.modules.item.ItemRepository;
import cacao.friends.shop.modules.member.CurrentMember;
import cacao.friends.shop.modules.member.Member;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CartController {
	
	private final ItemRepository itemRepository;
	
	private final CartRepository cartRepository;
	
	private final CartService cartService;
	
	@GetMapping("/cart")
	public String cartView(@CurrentMember Member member, Model model) {
		model.addAttribute("cartList", cartRepository.findWithItemByMember(member));
		return "member/cart";
	}
	
	@PostMapping("/cart/add/{itemId}")
	public String addCart(@CurrentMember Member member, @PathVariable Long itemId, CartForm cartForm, RedirectAttributes attributes) {
		Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("해당하는 상품이 없습니다."));
		cartService.createCart(item, member, cartForm.getQuantity());
		attributes.addFlashAttribute("message", "카트에 추가했습니다.");
		return "redirect:/item/" + itemId;
	}
	
	@PostMapping(value = "/cart/add/{itemId}",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addCart(@CurrentMember Member member, @PathVariable Long itemId, @RequestBody CartForm cartForm) {
		Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("해당하는 상품이 없습니다."));
		cartService.createCart(item, member, cartForm.getQuantity());
		return ResponseEntity.ok().body(cartRepository.countByMember(member));
	}
	
	@PostMapping("/cart/remove/{id}")
	public String removeCart(@PathVariable Long id) {
		Cart cart = cartRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당하는 카트가 없습니다."));
		cartService.remove(cart);
		return "redirect:/cart";
	}
	
	@PostMapping("/cart/remove")
	public String remove(@CurrentMember Member member, CartSearchForm form) {
		List<Cart> cartList = cartRepository.findAllById(form.getId());
		cartService.remove(cartList);
		return "redirect:/cart";
	}
	
	@PostMapping("/cart/remove/all")
	public String removeAll(@CurrentMember Member member) {
		cartService.removeAll(member);
		return "redirect:/cart";
	}
	
	@PostMapping(value = "/cart/edit/{id}", 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> editCart(@CurrentMember Member member, @PathVariable Long id, 
			@RequestBody CartForm cartForm) {
		Cart cart = cartRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당하는 카트가 없습니다."));
		cartService.updateCart(cart, cartForm.getQuantity());
		return ResponseEntity.ok().build();
	}
}
