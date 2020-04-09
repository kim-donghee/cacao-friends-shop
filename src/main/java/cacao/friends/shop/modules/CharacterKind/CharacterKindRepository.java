package cacao.friends.shop.modules.CharacterKind;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterKindRepository extends JpaRepository<CharacterKind, Long> {

	CharacterKind findByName(String name);

	boolean existsByName(String name);

}
