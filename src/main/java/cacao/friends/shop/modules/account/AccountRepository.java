package cacao.friends.shop.modules.account;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cacao.friends.shop.modules.characterKind.CharacterKind;

public interface AccountRepository extends JpaRepository<Account, Long> {

	Account findByUsername(String username);

	Account findByEmail(String email);

	Long countByEmailVerified(boolean emailVerified);

	boolean existsByEmail(String email);

	boolean existsByUsername(String username);
	
	List<Account> findByPickCharacter(CharacterKind characterKind);

}
