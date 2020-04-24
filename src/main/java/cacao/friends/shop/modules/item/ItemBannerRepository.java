package cacao.friends.shop.modules.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface ItemBannerRepository extends JpaRepository<ItemBanner, Long> {

}
