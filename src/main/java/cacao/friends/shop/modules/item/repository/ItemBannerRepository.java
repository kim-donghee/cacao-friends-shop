package cacao.friends.shop.modules.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.modules.item.ItemBanner;

@Transactional(readOnly = true)
public interface ItemBannerRepository extends JpaRepository<ItemBanner, Long> {

}
