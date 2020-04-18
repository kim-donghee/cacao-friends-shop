package cacao.friends.shop.modules.item;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemBannerService {

	private final ItemBannerRepository itemBannerRepository;
	
	public ItemBanner createBanner(String image) {
		return itemBannerRepository.save(ItemBanner.builder().image(image).build());
	}
	
}
