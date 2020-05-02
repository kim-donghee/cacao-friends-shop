package cacao.friends.shop.modules.characterKind;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.modules.characterKind.form.CharacterForm;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CharacterKindService {
	
	private final CharacterKindRepository tagRepository;
	
	private final ModelMapper modelMapper;
	
	public void createCharacter(CharacterForm form) {
		CharacterKind tag = modelMapper.map(form, CharacterKind.class);
		tagRepository.save(tag);
	}
	
	public void updateCharacter(CharacterKind character, CharacterForm form) {
		modelMapper.map(form, character);		
	}

	public void removeCharacter(CharacterKind character) {
		tagRepository.delete(character);
	}
	
	public boolean isValid(CharacterForm form) {
		String name = form.getName();
		String image = form.getImage();
		
		int nameLength = name.length();
		
		if(name == null || image == null || nameLength < 1 || nameLength > 50 || image.isEmpty()) {
			return false;
		}
		
		return true;
	}

}
