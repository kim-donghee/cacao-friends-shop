package cacao.friends.shop.modules.member;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cacao.friends.shop.modules.characterKind.CharacterKind;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Member findByUsername(String username);

	Member findByEmail(String email);
	
	Long countByEmailVerified(boolean emailVerified);

	boolean existsByEmail(String email);

	boolean existsByUsername(String username);
	
	List<Member> findByPickCharacter(CharacterKind characterKind);

}
