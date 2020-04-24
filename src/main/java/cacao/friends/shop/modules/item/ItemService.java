package cacao.friends.shop.modules.item;

import java.util.List;

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
	
	private final ItemBannerRepository itemBannerRepository;
	
	private final ItemCategoryRepository itemCategoryRepository;
	
	private final ApplicationEventPublisher eventPublisher;
	
	private final ModelMapper modelMapper;
	
	// 아이템 저장
	public Item createItem(ItemForm form) {
		return itemRepository.save(modelMapper.map(form, Item.class));
	}
	
	// 아이템 수정
	public void updateItem(Item item, ItemForm form) {
		modelMapper.map(form, item);
	}
	
	// 배너 저장
	public void addBanner(Item item, String image) {
		ItemBanner savedBanner = itemBannerRepository.save(ItemBanner.builder().image(image).build());
		item.addBanner(savedBanner);
	}
	
	// 배너 삭제
	public void removeBanner(Item item, ItemBanner banner) {
		item.removeBanner(banner);
	}
	
	// 메인 배너 수정
	public void updateMainBanner(Item item, String image) {
		item.setMainBanner(image);
	}
	
	// 카테고리 수정
	public void updateCategorys(Item item, List<Category> categorys) {
		itemCategoryRepository.deleteByItem(item.getId());
		
		categorys.forEach(c -> {
			addCategory(item, c);
		});
	}
	
	// 카테고리 추가
	public void addCategory(Item item, Category categorys) {
		ItemCategory itemCategory = ItemCategory.builder()
				.item(item)
				.category(categorys)
				.build();
		itemCategoryRepository.save(itemCategory);
	}
	
	// 카테고리 삭제
	public void removeCategory(Item item, ItemCategory itemCategory) {
		item.removeCategory(itemCategory);
	}
	
	// 캐릭터 수정
	public void updateCharacter(Item item, CharacterKind character) {
		item.setCharacter(character);
	}

	// 공개
	public void publish(Item item) {
		item.publish();
		eventPublisher.publishEvent(new ItemPublishEvent(item));
	}
	
	// 종료
	public void close(Item item) {
		item.close();
	}

	// 판매 정지
	public void pause(Item item) {
		item.pause();
	}

	// 재개
	public void resum(Item item) {
		item.resum();
	}
	
	// 미공개 전체 삭제
	public void undisclosedRemove() {
		itemRepository.undisclosedRemove();
	}

}
