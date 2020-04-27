package cacao.friends.shop.modules.cart;

import cacao.friends.shop.modules.item.Item;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Cart {
	
	private Item item;
	
	private int quantity;

}
