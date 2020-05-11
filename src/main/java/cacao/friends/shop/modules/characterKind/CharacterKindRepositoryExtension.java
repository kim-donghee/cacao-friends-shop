package cacao.friends.shop.modules.characterKind;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface CharacterKindRepositoryExtension {
	
	List<CharacterLastWeekOrderSaleDto> findLastWeekOrderSale(LocalDateTime from, LocalDateTime to);

	List<CharacterLastWeekOrderSaleDto> findOrderSale();
	
}
