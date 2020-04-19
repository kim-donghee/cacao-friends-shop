package cacao.friends.shop.modules.item;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.modules.category.Category;
import cacao.friends.shop.modules.characterKind.CharacterKind;
import cacao.friends.shop.modules.item.event.ItemPublishEvent;
import cacao.friends.shop.modules.item.form.ItemForm;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;
	
	private final ApplicationEventPublisher eventPublisher;
	
	private final ModelMapper modelMapper;
	
	public Item createItem(ItemForm form) {
		return itemRepository.save(modelMapper.map(form, Item.class));
	}
	
	public void updateItem(Item item, ItemForm form) {
		modelMapper.map(form, item);
	}
	
	public void addBanner(Item item, ItemBanner banner) {
		item.addBanner(banner);
	}
	
	public void removeBanner(Item item, ItemBanner banner) {
		item.removeBanner(banner);
	}
	
	public void updateMainBanner(Item item, String image) {
		item.setMainBanner(image);
	}
	
	public void updateCategorys(Item item, List<Category> categorys) {
		item.updateCategorys(categorys.stream().collect(Collectors.toSet()));
	}
	
	public void addCategory(Item item, Category category) {
		item.addCategory(category);
	}
	
	public void removeCategory(Item item, Category category) {
		item.removeCategory(category);
	}
	
	public void updateCharacter(Item item, CharacterKind character) {
		item.setCharacter(character);
	}

	public void publish(Item item) {
		item.publish();
		eventPublisher.publishEvent(new ItemPublishEvent(item));
	}

	public void close(Item item) {
		item.close();
	}

	public void pause(Item item) {
		item.pause();
	}

	public void resum(Item item) {
		item.resum();
	}

}
