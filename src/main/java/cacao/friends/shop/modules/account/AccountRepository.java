package cacao.friends.shop.modules.account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

	Account findByUsername(String username);

	Account findByEmail(String email);

	Long countByEmailVerified(boolean emailVerified);

	boolean existsByEmail(String email);

	boolean existsByUsername(String username);

}
