package cacao.friends.shop.modules.characterKind;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.modules.characterKind.form.CharacterForm;
import cacao.friends.shop.modules.characterKind.repository.CharacterKindRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CharacterKindService {
	
	private final CharacterKindRepository characterKindRepository;
	
	private final ModelMapper modelMapper;
	
	public void createCharacter(CharacterForm form) {
		CharacterKind tag = modelMapper.map(form, CharacterKind.class);
		characterKindRepository.save(tag);
	}
	
	public void updateCharacter(CharacterKind character, CharacterForm form) {
		modelMapper.map(form, character);		
	}

	public void removeCharacter(CharacterKind character) {
		characterKindRepository.delete(character);
	}

}
