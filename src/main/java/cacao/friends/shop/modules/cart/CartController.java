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
		model.addAttribute("cart", cartRepository.findWithItemByMember(member));
		return "member/cart";
	}
	
	@PostMapping(value = "/cart/add/{itemId}",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addCart(@CurrentMember Member member, @PathVariable Long itemId, @RequestBody CartForm cartForm) {
		Cart cart = cartRepository.findWithItemByMember(member);
		if(cart == null) {
			cart = cartService.newCart(member);
		}
		Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("해당하는 상품이 없습니다."));
		cartService.addCartItem(cart, item, cartForm.getQuantity());
		return ResponseEntity.ok().body(cartRepository.countCartItem(member));
	}
	
	@PostMapping("/cart/remove/{cartItemId}")
	public String removeCart(@CurrentMember Member member, @PathVariable Long cartItemId) {
		CartItem cartItem = cartRepository.findCartItem(member, cartItemId);
		cartService.removeItem(cartItem);
		return "redirect:/cart";
	}
	
	@PostMapping("/cart/remove")
	public String remove(@CurrentMember Member member, CartSearchForm cartSearchForm) {
		List<CartItem> cartList = cartRepository.findCartItem(member, cartSearchForm.getCartItemIds());
		cartService.removeItem(cartList);
		return "redirect:/cart";
	}
	
	@PostMapping("/cart/remove/all")
	public String removeAll(@CurrentMember Member member) {
		cartService.removeItemAll(member);
		return "redirect:/cart";
	}
	
	@PostMapping(value = "/cart/edit/{cartItemId}", 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> editCart(@CurrentMember Member member, @PathVariable Long cartItemId, 
			@RequestBody CartForm cartForm) {
		CartItem cartItem = cartRepository.findCartItem(member, cartItemId);
		cartService.changeQuantity(cartItem, cartForm.getQuantity());
		return ResponseEntity.ok().build();
	}
}
