package cacao.friends.shop.modules.tag;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.modules.tag.form.TagForm;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TagService {
	
	private final TagRepository tagRepository;
	
	private final ModelMapper modelMapper;
	
	public void createTag(TagForm tagForm) {
		Tag tag = modelMapper.map(tagForm, Tag.class);
		tagRepository.save(tag);
	}
	
	public void updateTag(Tag tag, TagForm tagForm) {
		modelMapper.map(tagForm, tag);		
	}

	public void removeTag(Tag tag) {
		tagRepository.delete(tag);
	}
	
	public boolean isValid(TagForm tagForm) {
		String name = tagForm.getName();
		String image = tagForm.getImage();
		
		int nameLength = name.length();
		
		if(name == null || image == null || nameLength < 2 || nameLength > 50 || image.isBlank()) {
			return false;
		}
		
		return true;
	}

}
