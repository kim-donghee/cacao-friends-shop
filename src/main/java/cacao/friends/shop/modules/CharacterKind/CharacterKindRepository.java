package cacao.friends.shop.modules.tag;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {

	Tag findByName(String name);

	boolean existsByName(String name);

}
