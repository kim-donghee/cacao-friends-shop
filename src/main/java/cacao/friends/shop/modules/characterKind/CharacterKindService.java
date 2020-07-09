package cacao.friends.shop.modules.characterKind;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.modules.characterKind.form.CharacterForm;
import cacao.friends.shop.modules.characterKind.repository.CharacterKindRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CharacterKindService {
	
	private final CharacterKindRepository repo;
	
	private final ModelMapper modelMapper;
	
	public void createCharacter(CharacterForm form) {
		CharacterKind tag = modelMapper.map(form, CharacterKind.class);
		repo.save(tag);
	}
	
	public void updateCharacter(Long id, CharacterForm form) {
		CharacterKind character = findById(id);
		modelMapper.map(form, character);		
	}

	public void removeCharacter(Long id) {
		CharacterKind character = findById(id);
		repo.delete(character);
	}
	
	@Transactional(readOnly = true)
	public List<CharacterKind> findAll() {
		return repo.findAll(Sort.by(Order.desc("id")));
	}
	
	private CharacterKind findById(Long id) {
		return repo.findById(id).orElseThrow(() 
				-> new IllegalArgumentException("해당하는 캐릭터가 없습니다."));
	}

}
