package cacao.friends.shop.modules.cart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cacao.friends.shop.modules.item.Item;
import cacao.friends.shop.modules.item.ItemRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {
	
	private final ItemRepository itemRepository;
	
	private final String CART_PREFIX = "item_number_";
	
	private final int ADD_UPDATE_AGE = 5 * 60 * 60;
	
	private final int REMOVE_AGE = 0;
	
	public void addCart(Long id, int quantity) {
		Cookie cookie = createCookie(id, quantity, ADD_UPDATE_AGE);
		getResponse().addCookie(cookie);
	}
	
	public void removeCart(Long id) {
		Cookie cookie = createCookie(id, 0, REMOVE_AGE);
		getResponse().addCookie(cookie);
	}
	
	public List<Cart> findCart() {
		Cookie[] allCookie = getRequest().getCookies();
		Map<Long, Integer> itemMap = new HashMap<>();
		List<Cart> cartList = new ArrayList<>();
		
		for(Cookie c : allCookie) {
			if(c.getName().startsWith(CART_PREFIX)) {
				Long itemId = Long.parseLong(c.getName().substring(c.getName().lastIndexOf("_") + 1));
				Integer quantity = Integer.parseInt(c.getValue());
				itemMap.put(itemId, quantity);
			}
		}
		
		for(Item i : itemRepository.findAllById(itemMap.keySet())) {
			Cart cart = new Cart();
			cart.setItem(i);
			cart.setQuantity(itemMap.get(i.getId()));
		}
		
		return cartList;
	}
	
	public int countCart() {
		Cookie[] allCookie = getRequest().getCookies();
		int count = 0;
		
		for(Cookie c : allCookie) {
			if(c.getName().startsWith(CART_PREFIX)) {
				count++;
			}
		}
		
		return count;
	}
	
	private Cookie createCookie(Long id, int quantity, int maxAge) {
		Cookie cookie = new Cookie(CART_PREFIX + id, String.valueOf(quantity));
		cookie.setMaxAge(maxAge);
		return cookie;
	}
	
	private ServletRequestAttributes getCurrentRequestAttributes() {
		return (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	}
	
	private HttpServletRequest getRequest() {
		HttpServletRequest request = getCurrentRequestAttributes().getRequest();
		return request;
	}
	
	private HttpServletResponse getResponse() {
		HttpServletResponse response = getCurrentRequestAttributes().getResponse();
		return response;
	}

}
