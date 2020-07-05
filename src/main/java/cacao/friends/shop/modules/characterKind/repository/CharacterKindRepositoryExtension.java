package cacao.friends.shop.modules.characterKind.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.modules.characterKind.CharacterOrderSaleDto;

@Transactional(readOnly = true)
public interface CharacterKindRepositoryExtension {
	
	List<CharacterOrderSaleDto> findLastWeekOrderSale(LocalDateTime from, LocalDateTime to);

	List<CharacterOrderSaleDto> findOrderSale();
	
}
