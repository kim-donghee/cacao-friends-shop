package cacao.friends.shop.modules.item;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.modules.member.Member;

@Transactional(readOnly = true)
public interface ItemRepositoryExtension {
	
	Page<Item> findByCondition(ItemCondition condition, Pageable pageable);
	
	Long countByCondition(ItemCondition condition);
	
	List<Item> findPopular();
	
	List<Item> findByMemberPick(Member currentMember);
	
}
