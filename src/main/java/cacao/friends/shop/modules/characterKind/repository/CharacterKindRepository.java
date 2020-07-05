package cacao.friends.shop.modules.characterKind.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.modules.characterKind.CharacterKind;

@Transactional(readOnly = true)
public interface CharacterKindRepository extends JpaRepository<CharacterKind, Long>, CharacterKindRepositoryExtension {

	CharacterKind findByName(String name);

	boolean existsByName(String name);

}
