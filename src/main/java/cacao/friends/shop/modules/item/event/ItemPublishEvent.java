package cacao.friends.shop.modules.item.event;

import cacao.friends.shop.modules.item.Item;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ItemPublishEvent {
	
	private final Item item;

}
