package cacao.friends.shop.modules.item;

import java.util.List;

import javax.persistence.EntityManager;

import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.modules.category.Category;
import cacao.friends.shop.modules.characterKind.CharacterKind;
import cacao.friends.shop.modules.item.event.ItemPublishEvent;
import cacao.friends.shop.modules.item.form.ItemForm;
import cacao.friends.shop.modules.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

	private final EntityManager em;
	
	private final ItemRepository itemRepository;
	
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
		item.addBanner(ItemBanner.builder().image(image).build());
	}
	
	// 배너 삭제
	public void removeBanner(Item item, ItemBanner banner) {
		item.removeBanner(banner);
	}
	
	// 메인 배너 수정
	public void updateMainBanner(Item item, ItemBanner banner) {
		item.updateMainBanner(banner.getImage());
	}
	
	// 카테고리 수정
	public void updateCategorys(Item item, List<Category> categories) {
		em
		.createQuery("DELETE FROM ItemCategory ic WHERE item_id = :item_id")
		.setParameter("item_id", item.getId()).executeUpdate();
		
		categories.forEach(c -> {
			ItemCategory itemCategory = ItemCategory.builder()
					.item(item)
					.category(c)
					.build();
			em.persist(itemCategory);
		});
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
