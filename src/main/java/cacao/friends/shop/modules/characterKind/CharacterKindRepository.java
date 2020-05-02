package cacao.friends.shop.modules.characterKind;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface CharacterKindRepository extends JpaRepository<CharacterKind, Long> {

	CharacterKind findByName(String name);

	boolean existsByName(String name);

}
