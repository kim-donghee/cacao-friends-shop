package cacao.friends.shop.modules.cart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cacao.friends.shop.modules.item.Item;
import cacao.friends.shop.modules.item.ItemRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartService {
	
	private final ItemRepository itemRepository;
	
	private final String CART_PREFIX = "item_number_";
	
	private final int ADD_UPDATE_AGE = 5 * 60 * 60;
	
	public void add(Long id, String quantity) {
		Cookie cookie = new Cookie(CART_PREFIX + id, quantity);
		cookie.setMaxAge(ADD_UPDATE_AGE);
		cookie.setPath("/");
		getResponse().addCookie(cookie);
	}
	
	public void remove(Long id) {
		Cookie cookie = new Cookie(CART_PREFIX + id, "");
		cookie.setMaxAge(0);
		cookie.setPath("/");
		getResponse().addCookie(cookie);
	}
	
	public void remove(List<Long> itemId) {
		itemId.forEach(id -> {
			remove(id);
		});
	}
	
	public void removeAll() {
		for(Cookie c : getRequest().getCookies()) {
			if(c.getName().startsWith(CART_PREFIX)) {
				Long itemId = Long.parseLong(c.getName().substring(c.getName().lastIndexOf("_") + 1));
				remove(itemId);
			}
		}
	}
	
	public List<Cart> findAll() {		
		List<Cart> cartList = new ArrayList<>();
		Map<Long, Integer> itemCookies = itemCookies();
		
		for(Item i : itemRepository.findAllById(itemCookies.keySet())) {
			Cart cart = new Cart();
			cart.setItem(i);
			cart.setQuantity(itemCookies.get(i.getId()));
			cartList.add(cart);
		}
		return cartList;
	}
	
	public List<Cart> findAllByItemId(List<Long> itemId) {		
		List<Cart> cartList = new ArrayList<>();
		Map<Long, Integer> itemCookies = itemCookies();
		
		for(Item i : itemRepository.findAllById(itemId)) {
			Cart cart = new Cart();
			cart.setItem(i);
			cart.setQuantity(itemCookies.get(i.getId()));
			cartList.add(cart);
		}
		return cartList;
	}
	
	public int countCart() {
		return itemCookies().size();
	}
	
	private Map<Long, Integer> itemCookies() {
		Cookie[] allCookie = getRequest().getCookies();
		Map<Long, Integer> map = new HashMap<>();
		
		if(allCookie == null) {
			return Collections.emptyMap();
		}
		
		for(Cookie c : allCookie) {
			if(c.getName().startsWith(CART_PREFIX)) {
				Long itemId = Long.parseLong(c.getName().substring(c.getName().lastIndexOf("_") + 1));
				Integer quantity = Integer.parseInt(c.getValue());
				map.put(itemId, quantity);
			}
		}
		
		return map;
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
